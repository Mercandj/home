package com.mercandalli.android.home

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException


class MainActivity : Activity() {

    private var gpio: Gpio? = null
    private var value: Boolean = false
    private val handler = Handler()
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Home Mercandalli"

        Log.d("jm/debug", "Java to jni on Android things: " + stringFromJNI())

        // Attempt to access the GPIO
        try {
            val manager = PeripheralManagerService()
            gpio = manager.openGpio("BCM7")
        } catch (e: IOException) {
            Log.w(TAG, "Unable to access GPIO", e)
        }

        runnable = Runnable { doJob() }
        handler.post(runnable)
    }

    fun doJob() {
        write(gpio!!, value)
        value = !value
        Log.d("jm/debug", "Read: " + read(gpio!!))
        handler.postDelayed(runnable, 1_000)
    }

    override fun onDestroy() {
        if (gpio != null) {
            try {
                gpio!!.close()
                gpio = null
            } catch (e: IOException) {
                Log.w(TAG, "Unable to close GPIO", e)
            }
        }

        super.onDestroy()
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

        @JvmStatic
        private fun write(gpio: Gpio, value: Boolean) {
            // Initialize the pin as a high output
            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
            // Low voltage is considered active
            gpio.setActiveType(Gpio.ACTIVE_HIGH)

            // Toggle the value to be LOW
            gpio.value = value
        }

        @JvmStatic
        private fun read(gpio: Gpio): Boolean {
            // Initialize the pin as an input
            gpio.setDirection(Gpio.DIRECTION_IN)
            // High voltage is considered active
            gpio.setActiveType(Gpio.ACTIVE_HIGH)

            // Read the active high pin state
            return gpio.value
        }
    }

}
