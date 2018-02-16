package com.mercandalli.core.weather

import com.mercandalli.core.main_thread.MainThreadPost

import okhttp3.OkHttpClient

class WeatherModule(
        private val okHttpClient: OkHttpClient,
        private val mainThreadPost: MainThreadPost) {

    fun provideWeatherManager(): WeatherManager {
        return WeatherManagerImpl(
                provideWeatherApi(),
                mainThreadPost)
    }

    private fun provideWeatherApi(): WeatherApi {
        return WeatherApiOkHttp(okHttpClient)
    }

}
