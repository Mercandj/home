package com.mercandalli.core.train

import org.json.JSONObject

data class TrainTraffic(
        val line: String,
        val slug: String,
        val title: String,
        val message: String,
        val date: String) {

    companion object {
        fun fromJson(json: JSONObject): TrainTraffic {
            val resultJson = json.getJSONObject("result")
            return TrainTraffic(
                    resultJson.getString("line"),
                    resultJson.getString("slug"),
                    resultJson.getString("title"),
                    resultJson.getString("message"),
                    json.getJSONObject("_metadata").getString("date"))
        }
    }

}
