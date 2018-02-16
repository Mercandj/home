package com.mercandalli.core.weather

interface WeatherManager {

    fun sync()

    fun getWeather(): Weather

    fun registerWeatherListener(listener: WeatherManager.WeatherListener)

    fun unregisterWeatherListener(listener: WeatherManager.WeatherListener)

    interface WeatherListener {
        fun onWeatherChanged()
    }

}