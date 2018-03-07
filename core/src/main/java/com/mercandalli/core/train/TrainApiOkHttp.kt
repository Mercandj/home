package com.mercandalli.core.train

import android.util.Log
import com.mercandalli.core.main.Closer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException

class TrainApiOkHttp(
        private val okHttpClient: OkHttpClient) : TrainApi {

    override fun getTrainTraffic(
            @TrainManager.Companion.TrainTrafficType trainTrafficType: Int): TrainTraffic? {
        val url = when (trainTrafficType) {
            TrainManager.TRAFFIC_A -> TrainConst.TRAFFIC_RER_A
            TrainManager.TRAFFIC_D -> TrainConst.TRAFFIC_RER_D
            TrainManager.TRAFFIC_9 -> TrainConst.TRAFFIC_METRO_9
            TrainManager.TRAFFIC_14 -> TrainConst.TRAFFIC_METRO_14
            else -> ""
        }
        val request = Request.Builder()
                .url(url)
                .build()
        var response: Response? = null
        var body: ResponseBody? = null
        try {
            response = okHttpClient.newCall(request).execute()
            body = response!!.body()
            return TrainTraffic.fromJson(trainTrafficType, JSONObject(body!!.string()))
        } catch (ignored: IOException) {
        } finally {
            Closer.closeSilently(body, response)
        }
        return null
    }

    override fun getTrainSchedules(trainSchedulesType: Int): TrainSchedules? {
        val url = when (trainSchedulesType) {
            TrainManager.SCHEDULES_GARE_DE_LYON_A -> TrainConst.SCHEDULES_GARE_DE_LYON_A
            TrainManager.SCHEDULES_BOISSY_A -> TrainConst.SCHEDULES_BOISSY_A
            TrainManager.SCHEDULES_YERRES_D -> TrainConst.SNCF_SCHEDULES_YERRES
            TrainManager.SCHEDULES_GARE_DE_LYON_D -> TrainConst.SNCF_SCHEDULES_GDL
            else -> ""
        }
        val request = if (trainSchedulesType == TrainManager.SCHEDULES_YERRES_D ||
                trainSchedulesType == TrainManager.SCHEDULES_GARE_DE_LYON_D) {
            Request.Builder()
                    .header("Authorization", "basic dG5odG4xMDQ6ck1IbTAzNmQ=")
                    .url(url)
                    .build()
        } else {
            Request.Builder()
                    .url(url)
                    .build()
        }
        var response: Response? = null
        var body: ResponseBody? = null
        try {
            response = okHttpClient.newCall(request).execute()
            body = response!!.body()
            if (trainSchedulesType == TrainManager.SCHEDULES_YERRES_D ||
                    trainSchedulesType == TrainManager.SCHEDULES_GARE_DE_LYON_D) {
                return TrainSchedules.fromXml(trainSchedulesType, body!!.string())
            }
            return TrainSchedules.fromJson(trainSchedulesType, JSONObject(body!!.string()))
        } catch (e: IOException) {
            Log.e("jm/debug", "Error", e)
        } finally {
            Closer.closeSilently(body, response)
        }
        return null
    }
}
