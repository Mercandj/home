package com.mercandalli.android.home.main

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.google.android.things.pio.Gpio
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mercandalli.android.home.R
import com.mercandalli.android.home.application.AppUtils.launchApp
import com.mercandalli.android.home.io_input_output_gpio.GpioManager
import com.mercandalli.android.home.io_input_output_gpio.GpioManagerImpl
import com.mercandalli.android.home.wifi.WifiUtils.Companion.wifiIpAddress

class MainActivity : AppCompatActivity() {

    private var gpio: Gpio? = null
    private var value: Boolean = false
    private val handler = Handler()
    private var runnableUpdateGpio7 = Runnable { runnableJob() }
    private var runnableUpdateDistance = Runnable { runnableDistance() }
    private var runnableDismissSnackbar = Runnable { snackbar?.dismiss() }
    private var gpio7TextView: TextView? = null
    private var distanceTextView: TextView? = null

    private val databaseReferenceGpio = FirebaseDatabase.getInstance().getReference("gpio")
    private val gpioManager = GpioManagerImpl.getInstanceInternal()
    private var gpio7RefreshRate = 300
    private val gpio7ValueEventListener = createGpio7ValueEventListener()

    private val databaseReferenceDistance = FirebaseDatabase.getInstance().getReference("distance")
    private val distanceValueEventListener = createDistanceValueEventListener()
    private var snackbar: Snackbar? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.activity_main_at_launcher)!!.setOnClickListener {
            launchApp(
                    this,
                    "com.android.iotlauncher",
                    "com.android.iotlauncher.DefaultIoTLauncher")
        }

        findViewById<TextView>(R.id.activity_main_ip)!!.text = wifiIpAddress(this).toString()
        gpio7TextView = findViewById(R.id.activity_main_gpio7)
        distanceTextView = findViewById(R.id.activity_main_distance_output)

        gpio = gpioManager.open(GpioManager.GPIO_7_NAME)


        handler.post(runnableUpdateGpio7)
        handler.post(runnableUpdateDistance)
        snackbar = Snackbar.make(window.decorView.findViewById(android.R.id.content),
                "Hello, there is something detected", Snackbar.LENGTH_INDEFINITE)

        databaseReferenceGpio.child("7").addValueEventListener(gpio7ValueEventListener)
        databaseReferenceDistance.child("on").addValueEventListener(distanceValueEventListener)

        if (savedInstanceState == null) {
            gpioManager.startDistanceMeasure()
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnableUpdateGpio7)
        handler.removeCallbacks(runnableUpdateDistance)
        handler.removeCallbacks(runnableDismissSnackbar)
        gpioManager.close(gpio!!)
        databaseReferenceGpio.child("7").removeEventListener(gpio7ValueEventListener)
        databaseReferenceDistance.child("on").removeEventListener(distanceValueEventListener)
        super.onDestroy()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_F1 -> {

                return true
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun runnableJob() {
        gpioManager.write(gpio!!, value)
        gpio7TextView!!.text = "Gpio7 rate $gpio7RefreshRate ms : " + if (value) "on" else "off"
        value = !value
        syncDistance()
        handler.removeCallbacks(runnableUpdateGpio7)
        handler.postDelayed(runnableUpdateGpio7, gpio7RefreshRate.toLong())
    }

    private fun runnableDistance() {
        syncDistance()
        handler.removeCallbacks(runnableUpdateDistance)
        handler.postDelayed(runnableUpdateDistance, 80)
    }

    private fun syncDistance() {
        val distanceInt = gpioManager.getDistance()
        databaseReferenceDistance.child("value").setValue(distanceInt)
        distanceTextView!!.text = "Distance: $distanceInt cm"
        if (distanceInt < 40) {
            handler.removeCallbacks(runnableDismissSnackbar)
            snackbar!!.show()
        } else {
            handler.postDelayed(runnableDismissSnackbar, 1_500)
        }
    }

    private fun createGpio7ValueEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gpio7RefreshRate = dataSnapshot.getValue<Int>(Int::class.java)!!
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        }
    }

    private fun createDistanceValueEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                /*
                val distanceOn = dataSnapshot.getValue<Boolean>(Boolean::class.java)!!
                if (distanceOn) {
                    gpioManager.startDistanceMeasure()
                } else {
                    gpioManager.stopDistanceMeasure()
                }
                */
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
