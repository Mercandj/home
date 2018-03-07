package com.mercandalli.core.weather

import com.mercandalli.core.main.Closer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException

class WeatherApiOkHttp(
        private val okHttpClient: OkHttpClient) : WeatherApi {

    override fun getWeather(): Weather {
        val request = Request.Builder()
                .url(WeatherConst.WEATHER_BILLANCOURT)
                .build()
        var response: Response? = null
        var body: ResponseBody? = null
        try {
            response = okHttpClient.newCall(request).execute()
            body = response!!.body()
            return Weather.fromJson(JSONObject(body!!.string()))
        } catch (ignored: IOException) {
        } finally {
            Closer.closeSilently(body, response)
        }
        return Weather(-42.0, 1000)
    }
}
