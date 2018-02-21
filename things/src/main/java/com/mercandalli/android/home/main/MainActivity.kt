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
import com.google.android.things.pio.PeripheralManagerService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mercandalli.android.home.R
import com.mercandalli.android.home.application.AppUtils.launchApp
import com.mercandalli.android.home.io_input_output_gpio.GpioManager
import com.mercandalli.android.home.io_input_output_gpio.GpioManager.Companion.ECHO_PIN_NAME
import com.mercandalli.android.home.io_input_output_gpio.GpioManager.Companion.TRIGGER_PIN_NAME
import com.mercandalli.android.home.io_input_output_gpio.GpioManagerImpl
import com.mercandalli.android.home.wifi.WifiUtils.Companion.wifiIpAddress
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var gpio: Gpio? = null
    private var value: Boolean = false
    private val handler = Handler()
    private var runnableUpdateGpio7: Runnable? = null
    private var gpio7TextView: TextView? = null
    private var distanceTextView: TextView? = null

    private val databaseReferenceGpio = FirebaseDatabase.getInstance().getReference("gpio")
    private val gpioManager = GpioManagerImpl.instance
    private var gpio7RefreshRate = 300
    private val gpio7ValueEventListener = createGpio7ValueEventListener()

    private val databaseReferenceDistance = FirebaseDatabase.getInstance().getReference("distance")
    private var distanceOn = true
    private val distanceValueEventListener = createDistanceValueEventListener()

    private var echoGpio: Gpio? = null
    private var triggerGpio: Gpio? = null

    private var time1: Long = 0
    private var time2: Long = 0
    private var keepBusy: Int = 0

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

        findViewById<TextView>(R.id.activity_main_ip)!!.text = "Ip: " + wifiIpAddress(this)
        gpio7TextView = findViewById(R.id.activity_main_gpio7)
        distanceTextView = findViewById(R.id.activity_main_distance_output)

        gpio = gpioManager.open(GpioManager.GPIO_7_NAME)
        runnableUpdateGpio7 = Runnable { runnableJob() }
        handler.post(runnableUpdateGpio7)

        databaseReferenceGpio.child("7").addValueEventListener(gpio7ValueEventListener)
        databaseReferenceDistance.child("on").addValueEventListener(distanceValueEventListener)

        if (savedInstanceState == null) {
            // Initialize PeripheralManagerService
            val service = PeripheralManagerService()

            // List all available GPIOs
            Log.d("jm/debug", "Available GPIOs: " + service.gpioList)

            try {
                // Create GPIO connection.
                echoGpio = service.openGpio(ECHO_PIN_NAME)
                // Configure as an input.
                echoGpio!!.setDirection(Gpio.DIRECTION_IN)
                // Enable edge trigger events.
                echoGpio!!.setEdgeTriggerType(Gpio.EDGE_BOTH)
                // Set Active type to HIGH, then the HIGH events will be considered as TRUE
                echoGpio!!.setActiveType(Gpio.ACTIVE_HIGH)

            } catch (e: IOException) {
                Log.e("jm/debug", "Error on PeripheralIO API", e)
            }

            try {
                // Create GPIO connection.
                triggerGpio = service.openGpio(TRIGGER_PIN_NAME)

                // Configure as an output with default LOW (false) value.
                triggerGpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

            } catch (e: IOException) {
                Log.e("jm/debug", "Error on PeripheralIO API", e)
            }
        }
    }

    private fun readDistance() {
        if (!distanceOn) {
            return
        }
        Thread(Runnable {
            readDistanceSync()
        }).start()
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun readDistanceSync() {
        // Just to be sure, set the trigger first to false
        triggerGpio!!.value = false
        Thread.sleep(0, 2000)

        // Hold the trigger pin HIGH for at least 10 us
        triggerGpio!!.value = true
        Thread.sleep(0, 10000) //10 microsec

        // Reset the trigger pin
        triggerGpio!!.value = false

        // Wait for pulse on ECHO pin
        while (!echoGpio!!.value) {
            //long t1 = System.nanoTime();
            //Log.d(TAG, "Echo has not arrived...");

            // keep the while loop busy
            keepBusy = 0

            //long t2 = System.nanoTime();
            //Log.d(TAG, "diff 1: " + (t2-t1));
        }
        time1 = System.nanoTime()
        Log.i("jm/debug", "Echo ARRIVED!")

        // Wait for the end of the pulse on the ECHO pin
        while (echoGpio!!.value) {
            //long t1 = System.nanoTime();
            //Log.d(TAG, "Echo is still coming...");

            // keep the while loop busy
            keepBusy = 1

            //long t2 = System.nanoTime();
            //Log.d(TAG, "diff 2: " + (t2-t1));
        }
        time2 = System.nanoTime()
        Log.i("jm/debug", "Echo ENDED!")

        // Measure how long the echo pin was held high (pulse width)
        val pulseWidth = time2 - time1

        // Calculate distance in centimeters. The constants
        // are coming from the datasheet, and calculated from the assumed speed
        // of sound in air at sea level (~340 m/s).
        val distance = pulseWidth / 1000.0 / 58.23 //cm

        // or we could calculate it withe the speed of the sound:
        //double distance = (pulseWidth / 1000000000.0) * 340.0 / 2.0 * 100.0;

        Log.i("jm/debug", "distance: $distance cm")
        runOnUiThread {
            val distanceInt = distance.toInt()
            databaseReferenceDistance.child("value").setValue(distanceInt)
            distanceTextView!!.text = "Distance rate $gpio7RefreshRate : $distanceInt cm"
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnableUpdateGpio7)
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

        readDistance()

        handler.removeCallbacks(runnableUpdateGpio7)
        handler.postDelayed(runnableUpdateGpio7, gpio7RefreshRate.toLong())
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
                distanceOn = dataSnapshot.getValue<Boolean>(Boolean::class.java)!!
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
