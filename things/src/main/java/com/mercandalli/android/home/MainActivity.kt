package com.mercandalli.android.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder

class MainActivity : Activity() {

    private var gpio: Gpio? = null
    private var value: Boolean = false
    private val handler = Handler()
    private var runnable: Runnable? = null
    private var gpio7TextView: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Home Mercandalli"

        Log.d("jm/debug", "Java to jni on Android things: " + stringFromJNI())

        findViewById<TextView>(R.id.activity_main_ip)!!.text = "Ip: " + wifiIpAddress(this)
        gpio7TextView = findViewById(R.id.activity_main_gpio7)

        // Attempt to access the GPIO
        try {
            val manager = PeripheralManagerService()
            gpio = manager.openGpio("BCM7")
        } catch (e: IOException) {
            Log.w(TAG, "Unable to access GPIO", e)
        }

        runnable = Runnable { runnableJob() }
        handler.post(runnable)
    }

    private fun runnableJob() {
        write(gpio!!, value)
        value = !value
        val read = read(gpio!!)
        gpio7TextView!!.text = if (read) "Gpio7: rp3 output -> ON" else "Gpio7: rp3 output -> OFF"
        Log.d("jm/debug", "Read: " + read)
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

        @JvmStatic
        private fun wifiIpAddress(context: Context): String? {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            var ipAddress = wifiManager.connectionInfo.ipAddress

            // Convert little-endian to big-endianif needed
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                ipAddress = Integer.reverseBytes(ipAddress)
            }

            val ipByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()

            val ipAddressString: String?
            ipAddressString = try {
                InetAddress.getByAddress(ipByteArray).hostAddress
            } catch (ex: UnknownHostException) {
                Log.e("WIFIIP", "Unable to get host address.")
                null
            }

            return ipAddressString
        }
    }

}
