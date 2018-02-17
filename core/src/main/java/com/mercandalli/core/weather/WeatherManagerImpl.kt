package com.mercandalli.core.weather

import com.mercandalli.core.main_thread.MainThreadPost
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

internal class WeatherManagerImpl(
        private val weatherApi: WeatherApi,
        private val mainThreadPost: MainThreadPost) : WeatherManager {

    private val listeners = ArrayList<WeatherManager.WeatherListener>()

    private var weather = Weather(-42.0, 1000)

    override fun sync() {
        async(CommonPool) {
            innerSync()
        }
    }

    override fun getWeather(): Weather {
        return weather
    }

    override fun registerWeatherListener(listener: WeatherManager.WeatherListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterWeatherListener(listener: WeatherManager.WeatherListener) {
        listeners.remove(listener)
    }

    private fun notifyListener(weather: Weather) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable {
                notifyListener(weather)
            })
            return
        }
        this.weather = weather
        for (listener in listeners) {
            listener.onWeatherChanged()
        }
    }

    private suspend fun innerSync() {
        val weatherDelegate = async {
            val innerWeather = weatherApi.getWeather()
            innerWeather
        }
        notifyListener(weatherDelegate.await())
    }

}
