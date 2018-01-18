package com.mercandalli.android.home.io_input_output_gpio

import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import com.mercandalli.android.home.log.LogManager
import com.mercandalli.android.home.log.LogManagerImpl
import java.io.IOException

class GpioManagerImpl private constructor(
        private val peripheralManagerService: PeripheralManagerService,
        private val logManager: LogManager) : GpioManager {

    override fun open(name: String): Gpio {
        var gpio: Gpio? = null
        try {
            gpio = peripheralManagerService.openGpio(name)
        } catch (e: IOException) {
            logManager.log(TAG, "open error", e)
        }

        return gpio!!
    }

    override fun close(gpio: Gpio?) {
        if (gpio != null) {
            try {
                gpio.close()
            } catch (e: IOException) {
                logManager.log(TAG, "close error", e)
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
            logManager.log(TAG, "write error", e)
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
            logManager.log(TAG, "Error read gpio " + gpio, e)
        }

        return false
    }

    companion object {

        private val TAG = "GpioManager"

        @JvmStatic
        val instance: GpioManager = getInstanceInternal()

        private fun getInstanceInternal(): GpioManager {
            val logManager = LogManagerImpl.instance
            return GpioManagerImpl(
                    PeripheralManagerService(),
                    logManager
            )
        }
    }
}