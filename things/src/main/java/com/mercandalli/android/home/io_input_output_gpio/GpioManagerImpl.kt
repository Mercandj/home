package com.mercandalli.android.home.io_input_output_gpio

import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException

class GpioManagerImpl private constructor(
        private val peripheralManagerService: PeripheralManagerService) : GpioManager {

    override fun open(name: String): Gpio {
        var gpio: Gpio? = null
        try {
            gpio = peripheralManagerService.openGpio(name)
        } catch (e: IOException) {
            Log.e(TAG, "open error", e)
        }

        return gpio!!
    }

    override fun close(gpio: Gpio?) {
        if (gpio != null) {
            try {
                gpio.close()
            } catch (e: IOException) {
                Log.e(TAG, "close error", e)
            }
        }
    }

    override fun write(gpio: Gpio, on: Boolean) {
        // Initialize the pin as a high output
        try {
            gpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)

            // Low voltage is considered active
            gpio.setActiveType(Gpio.ACTIVE_HIGH)

            // Toggle the value to be LOW
            gpio.value = on
        } catch (e: IOException) {
            Log.e(TAG, "write error", e)
        }
    }

    override fun read(gpio: Gpio): Boolean {
        // Initialize the pin as an input
        try {
            gpio.setDirection(Gpio.DIRECTION_IN)

            // High voltage is considered active
            gpio.setActiveType(Gpio.ACTIVE_HIGH)

            // Read the active high pin state
            return gpio.value
        } catch (e: IOException) {
            Log.e(TAG, "Error read gpio " + gpio, e)
        }
        return false
    }
    override fun startDistanceMeasure() {

    }

    override fun stopDistanceMeasure() {

    }

    override fun distanceMeasureSync() {

    }

    companion object {

        private val TAG = "GpioManager"

        @JvmStatic
        val instance: GpioManager = getInstanceInternal()

        private fun getInstanceInternal(): GpioManager {
            return GpioManagerImpl(
                    PeripheralManagerService())
        }
    }
}
