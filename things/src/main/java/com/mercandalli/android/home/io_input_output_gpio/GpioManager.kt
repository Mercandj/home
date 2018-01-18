package com.mercandalli.android.home.io_input_output_gpio

import com.google.android.things.pio.Gpio

interface GpioManager {

    fun open(name: String): Gpio

    fun close(gpio: Gpio?)

    fun write(gpio: Gpio, on: Boolean)

    fun read(gpio: Gpio): Boolean

    companion object {

        val GPIO_7_NAME = "BCM7"
    }

}
