package com.mercandalli.android.home.main

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.google.android.things.pio.Gpio
import com.mercandalli.android.home.R
import com.mercandalli.android.home.application.AppUtils.launchApp
import com.mercandalli.android.home.io_input_output_gpio.GpioManager
import com.mercandalli.android.home.io_input_output_gpio.GpioManagerImpl
import com.mercandalli.android.home.log.LogManagerImpl
import com.mercandalli.android.home.wifi.WifiUtils.Companion.wifiIpAddress

class MainActivity : Activity() {

    private var gpio: Gpio? = null
    private var value: Boolean = false
    private val handler = Handler()
    private var runnable: Runnable? = null
    private var gpio7TextView: TextView? = null
    private var logs: TextView? = null

    private val gpioManager = GpioManagerImpl.instance
    private val logManager = LogManagerImpl.instance

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Android things  -  Raspberry pi 3"

        Log.d("jm/debug", "Java to jni on Android things: " + stringFromJNI())

        findViewById<View>(R.id.activity_main_at_launcher)!!.setOnClickListener {
            launchApp(
                    this,
                    "com.android.iotlauncher",
                    "com.android.iotlauncher.DefaultIoTLauncher")
        }

        logs = findViewById(R.id.activity_main_logs)
        logs!!.movementMethod = ScrollingMovementMethod()
        findViewById<TextView>(R.id.activity_main_ip)!!.text = "Ip: " + wifiIpAddress(this)
        gpio7TextView = findViewById(R.id.activity_main_gpio7)

        gpio = gpioManager.open(GpioManager.GPIO_7_NAME)
        runnable = Runnable { runnableJob() }
        handler.post(runnable)
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
        logs!!.text = logManager.systemLogs
        handler.postDelayed(runnable, 1_000)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        gpioManager.close(gpio!!)
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
    }

}
