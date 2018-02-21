package com.mercandalli.android.home.io_input_output_gpio

import com.google.android.things.pio.Gpio

interface GpioManager {

    fun open(name: String): Gpio

    fun close(gpio: Gpio?)

    fun write(gpio: Gpio, on: Boolean)

    fun read(gpio: Gpio): Boolean

    fun startDistanceMeasure()

    fun stopDistanceMeasure()

    fun distanceMeasureSync()

    companion object {

        const val GPIO_7_NAME = "BCM7"

        const val TRIGGER_PIN_NAME = "BCM23"
        const val ECHO_PIN_NAME = "BCM24"
    }

}
