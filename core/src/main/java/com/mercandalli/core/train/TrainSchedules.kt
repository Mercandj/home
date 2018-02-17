package com.mercandalli.core.train

import org.json.JSONObject

data class TrainSchedules(
        @TrainManager.Companion.TrainSchedulesType val trainSchedulesType: Long,
        val schedules: List<TrainSchedule>,
        val date: String) {

    companion object {
        fun fromJson(
                @TrainManager.Companion.TrainTrafficType trainTrafficType: Long,
                json: JSONObject): TrainSchedules {
            val trainSchedules = ArrayList<TrainSchedule>()
            val trainSchedulesJsonArray = json.getJSONObject("result").getJSONArray("schedules")
            (0 until trainSchedulesJsonArray.length())
                    .mapTo(trainSchedules) {
                        TrainSchedule.fromJson(trainSchedulesJsonArray.getJSONObject(it))
                    }
            return TrainSchedules(
                    trainTrafficType,
                    trainSchedules,
                    json.getJSONObject("_metadata").getString("date"))
        }
    }

}
