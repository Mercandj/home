package com.mercandalli.core.train

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
            @TrainManager.Companion.TrainTrafficType trainTrafficType: Long): TrainTraffic? {
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

}
