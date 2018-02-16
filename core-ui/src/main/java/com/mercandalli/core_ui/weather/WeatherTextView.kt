package com.mercandalli.core_ui.weather

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core.weather.WeatherManager
import java.util.concurrent.TimeUnit

class WeatherTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), WeatherManager.WeatherListener {

    private val refreshRate = TimeUnit.MINUTES.toMillis(100)
    private lateinit var weatherManager: WeatherManager

    private val syncRunnable = Runnable {
        syncRequest()
        postSync()
    }

    init {
        if (!isInEditMode) {
            weatherManager = CoreGraph.get().provideWeatherManager()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode) {
            return
        }
        internalResume()
        attachToWeatherManager()
        weatherManager.sync()
    }

    override fun onDetachedFromWindow() {
        if (isInEditMode) {
            super.onDetachedFromWindow()
            return
        }
        internalPause()
        detachFromClockManager()
        super.onDetachedFromWindow()
    }

    override fun onWeatherChanged() {
        syncView()
    }

    private fun attachToWeatherManager() {
        syncView()
        weatherManager.registerWeatherListener(this)
        syncRequest()
        syncView()
        postSync()
    }

    private fun detachFromClockManager() {
        weatherManager.unregisterWeatherListener(this)
    }

    private fun internalResume() {
        if (!isInEditMode) {
            attachToWeatherManager()
        }
    }

    private fun internalPause() {
        if (!isInEditMode) {
            detachFromClockManager()
        }
    }

    private fun postSync() {
        removeCallbacks(syncRunnable)
        postDelayed(syncRunnable, refreshRate)
    }

    private fun syncView() {
        val weather = weatherManager.getWeather()
        text = weather.temperatureC.toInt().toString() + "°C\n" + weather.pressure + " hPa"
    }

    private fun syncRequest() {
        weatherManager.sync()
    }
}
