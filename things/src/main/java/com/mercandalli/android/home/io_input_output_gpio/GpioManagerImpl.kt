package com.mercandalli.android.home.io_input_output_gpio

import android.content.ContentValues
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core.main_thread.MainThreadPost
import java.io.IOException

class GpioManagerImpl private constructor(
        private val peripheralManagerService: PeripheralManagerService,
        private val mainThreadPost: MainThreadPost) : GpioManager {

    private var echoGpio: Gpio? = null
    private var triggerGpio: Gpio? = null

    private var time1: Long = 0
    private var time2: Long = 0
    private var keepBusy: Int = 0

    private var measureDistanceOn = false
    private var distancesMeasured = ArrayList<Double>()

    private val thread = Thread(Runnable {
        while (true) {
            if (measureDistanceOn) {
                try {
                    readDistanceSync()
                    Thread.sleep(1)
                } catch (e: IOException) {
                    Log.e(TAG, "IOException thread", e)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "InterruptedException thread", e)
                }
            } else {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Log.e(ContentValues.TAG, "InterruptedException 2 thread", e)
                }
            }
        }
    })

    init {
        // Initialize PeripheralManagerService
        val service = PeripheralManagerService()

        // List all available GPIOs
        Log.d(TAG, "INIT coucou Available GPIOs: " + service.gpioList)

        try {
            // Create GPIO connection.
            echoGpio = service.openGpio(GpioManager.ECHO_PIN_NAME)
            // Configure as an input.
            echoGpio!!.setDirection(Gpio.DIRECTION_IN)
            // Enable edge trigger events.
            echoGpio!!.setEdgeTriggerType(Gpio.EDGE_BOTH)
            // Set Active type to HIGH, then the HIGH events will be considered as TRUE
            echoGpio!!.setActiveType(Gpio.ACTIVE_HIGH)

        } catch (e: IOException) {
            Log.e(TAG, "Error on PeripheralIO API", e)
        }

        try {
            // Create GPIO connection.
            triggerGpio = service.openGpio(GpioManager.TRIGGER_PIN_NAME)

            // Configure as an output with default LOW (false) value.
            triggerGpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

        } catch (e: IOException) {
            Log.e(TAG, "Error on PeripheralIO API", e)
        }

        thread.priority = Thread.MAX_PRIORITY
        thread.start()
    }

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
        measureDistanceOn = true
    }

    override fun stopDistanceMeasure() {
        measureDistanceOn = false
    }

    override fun getDistance(): Int {
        if (distancesMeasured.isEmpty()) {
            return -1
        }
        val size = distancesMeasured.size
        if (size < 5) {
            return distancesMeasured[size - 1].toInt()
        }
        val subList = distancesMeasured.subList(size - 5, size - 1)
        val min = subList.min()
        val max = subList.max()
        subList.remove(min)
        subList.remove(max)
        return subList.average().toInt()
    }

    @Throws(IOException::class, InterruptedException::class)
    private fun readDistanceSync() {
        Log.d(TAG, "readDistanceSync")

        // Just to be sure, set the trigger first to false
        triggerGpio!!.value = false
        Thread.sleep(0, 2_000)

        // Hold the trigger pin HIGH for at least 10 us
        triggerGpio!!.value = true
        Thread.sleep(0, 10_000) //10 microsec

        // Reset the trigger pin
        triggerGpio!!.value = false

        val time0 = System.nanoTime()

        // Wait for pulse on ECHO pin
        while (!echoGpio!!.value && System.nanoTime() - time0 < 6_000_000) {
            //long t1 = System.nanoTime();
            Log.d(TAG, "Echo has not arrived... " + (System.nanoTime() - time0))

            // keep the while loop busy
            keepBusy = 0

            //long t2 = System.nanoTime();
            //Log.d(TAG, "diff 1: " + (t2-t1));
        }
        time1 = System.nanoTime()
        Log.i(TAG, "Echo ARRIVED!")

        // Wait for the end of the pulse on the ECHO pin
        while (echoGpio!!.value && System.nanoTime() - time1 < 6_000_000) {
            //long t1 = System.nanoTime()
            Log.d(TAG, "Echo is still coming... " + (System.nanoTime() - time1))

            // keep the while loop busy
            keepBusy = 1

            //long t2 = System.nanoTime()
            Log.d(TAG, "diff 2: " + (System.nanoTime() - time1))
        }
        time2 = System.nanoTime()
        Log.i(TAG, "Echo ENDED!")

        // Measure how long the echo pin was held high (pulse width)
        val pulseWidth = time2 - time1

        // Calculate distance in centimeters. The constants
        // are coming from the datasheet, and calculated from the assumed speed
        // of sound in air at sea level (~340 m/s).
        val distance = pulseWidth / 1000.0 / 58.23 //cm

        // or we could calculate it withe the speed of the sound:
        //double distance = (pulseWidth / 1000000000.0) * 340.0 / 2.0 * 100.0;

        Log.i(TAG, "distance: $distance cm")
        mainThreadPost.post(Runnable {
            if (distancesMeasured.size > 500) {
                distancesMeasured.clear()
            }
            distancesMeasured.add(distance)
        })
    }

    companion object {

        private val TAG = "GpioManager jm/debug"

        @JvmStatic
        private var instance: GpioManager? = null

        fun getInstanceInternal(): GpioManager {
            if (instance == null) {
                Log.d(TAG, "Create GpioManagerImpl")
                instance = GpioManagerImpl(
                        PeripheralManagerService(),
                        CoreGraph.get().provideMainThreadPost())

            }
            return instance!!
        }
    }
}
