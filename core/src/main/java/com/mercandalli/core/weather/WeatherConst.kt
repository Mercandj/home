package com.mercandalli.core.weather

class WeatherConst {

    companion object {
        private const val API_ID = "886705b4c1182eb1c69f28eb8c520e20"
        const val WEATHER_BILLANCOURT = "http://api.openweathermap.org/data/2.5/weather?appid=$API_ID&units=metric&q=Billancourt"

        /**
         * Date: 2016-12-25T01:04:08Z
         */
        @JvmStatic
        fun getAirPollutionUrl(date: String): String {
            return "http://api.openweathermap.org/pollution/v1/co/48.8321071,2.2384558/$date.json?appid=$API_ID"
        }
    }

}