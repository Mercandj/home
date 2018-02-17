package com.mercandalli.core.train

import org.json.JSONObject
import org.json.XML


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

        fun fromXml(
                @TrainManager.Companion.TrainTrafficType trainTrafficType: Long,
                xmlString: String): TrainSchedules {
            val schedules = ArrayList<TrainSchedule>()
            val json = XML.toJSONObject(xmlString)
            val trainArray = json.getJSONObject("passages").getJSONArray("train")
            for (i in 0 until trainArray.length()) {
                val trainJsonObject = trainArray.getJSONObject(i)
                val date = trainJsonObject.getJSONObject("date").getString("content")
                val miss = trainJsonObject.getString("miss")
                val term = trainJsonObject.getString("term")
                val num = trainJsonObject.getString("num")
                schedules.add(TrainSchedule(
                        when (term) {
                            TrainConst.MELUN_UIC -> "Melun"
                            TrainConst.YERRES_UIC -> "Yerres"
                            TrainConst.GDL_UIC_1 -> "Gare de lyon 1"
                            TrainConst.GDL_UIC_2 -> "Gare de lyon 2"
                            else -> "Term: " + term
                        },
                        "" + date,
                        miss + " - " + num))
            }
            return TrainSchedules(
                    trainTrafficType,
                    schedules,
                    ""
            )
        }
    }

}
