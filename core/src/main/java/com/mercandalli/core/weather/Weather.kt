package com.mercandalli.core.weather

import org.json.JSONObject

data class Weather(
        val temperatureC: Double,
        val pressure: Int) {

    companion object {
        fun fromJson(json: JSONObject): Weather {
            val main = json.getJSONObject("main")
            return Weather(
                    main.getDouble("temp"),
                    main.getInt("pressure"))
        }
    }

}
