package com.mercandalli.core.train

import org.json.JSONObject

data class TrainSchedule(
        val code: String,
        val message: String,
        val destination: String) {

    companion object {
        fun fromJson(json: JSONObject): TrainSchedule {
            return TrainSchedule(
                    json.getString("code"),
                    json.getString("message"),
                    json.getString("destination"))
        }
    }

}
