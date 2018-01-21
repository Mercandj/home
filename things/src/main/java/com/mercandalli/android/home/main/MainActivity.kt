package com.mercandalli.android.home.main

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
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
    private var runnableUpdateGpio7: Runnable? = null
    private var gpio7TextView: TextView? = null

    private val gpioManager = GpioManagerImpl.instance
    private var gpio7RefreshRate = 300

    private val valueEventListener = createValueEventListener()
    private val databaseReferenceGpio = FirebaseDatabase.getInstance().getReference("gpio")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))
        title = "Android things  -  Raspberry pi 3"

        findViewById<View>(R.id.activity_main_at_launcher)!!.setOnClickListener {
            launchApp(
                    this,
                    "com.android.iotlauncher",
                    "com.android.iotlauncher.DefaultIoTLauncher")
        }

        findViewById<TextView>(R.id.activity_main_ip)!!.text = "Ip: " + wifiIpAddress(this)
        gpio7TextView = findViewById(R.id.activity_main_gpio7)

        gpio = gpioManager.open(GpioManager.GPIO_7_NAME)
        runnableUpdateGpio7 = Runnable { runnableJob() }
        handler.post(runnableUpdateGpio7)

        databaseReferenceGpio.child("7").addValueEventListener(valueEventListener)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnableUpdateGpio7)
        gpioManager.close(gpio!!)
        databaseReferenceGpio.child("7").removeEventListener(valueEventListener)
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
        gpio7TextView!!.text = "Gpio7 rate " + gpio7RefreshRate + " ms : " + if (value) "on" else "off"
        value = !value

        handler.removeCallbacks(runnableUpdateGpio7)
        handler.postDelayed(runnableUpdateGpio7, gpio7RefreshRate.toLong())
    }

    private fun createValueEventListener(): ValueEventListener {
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
