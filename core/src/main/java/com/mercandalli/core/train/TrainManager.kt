package com.mercandalli.core.train

import android.support.annotation.IntDef

interface TrainManager {

    companion object {

        @IntDef(
                TRAFFIC_A,
                TRAFFIC_D,
                TRAFFIC_9,
                TRAFFIC_14)
        @Retention(AnnotationRetention.SOURCE)
        annotation class TrainTrafficType

        const val TRAFFIC_A = 0L
        const val TRAFFIC_D = 1L
        const val TRAFFIC_9 = 2L
        const val TRAFFIC_14 = 3L
    }

    fun sync()

    fun getTrainTraffic(@TrainTrafficType trainTrafficType: Long): TrainTraffic?

    fun registerTrainTrafficListener(listener: TrainManager.TrainTrafficListener)

    fun unregisterTrainTrafficListener(listener: TrainManager.TrainTrafficListener)

    interface TrainTrafficListener {
        fun onTrainTrafficChanged(@TrainTrafficType trainTrafficType: Long)
    }


}