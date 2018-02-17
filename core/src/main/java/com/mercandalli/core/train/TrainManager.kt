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

        @IntDef(
                SCHEDULES_GARE_DE_LYON_A,
                SCHEDULES_BOISSY_A)
        @Retention(AnnotationRetention.SOURCE)
        annotation class TrainSchedulesType

        const val SCHEDULES_GARE_DE_LYON_A = 0L
        const val SCHEDULES_BOISSY_A = 1L
    }

    fun sync()

    fun getTrainTraffic(@TrainTrafficType trainTrafficType: Long): TrainTraffic?

    fun registerTrainTrafficListener(listener: TrainManager.TrainTrafficListener)

    fun unregisterTrainTrafficListener(listener: TrainManager.TrainTrafficListener)

    fun getTrainSchedules(@TrainSchedulesType trainSchedulesType: Long): TrainSchedules?

    fun registerTrainSchedulesListener(listener: TrainManager.TrainSchedulesListener)

    fun unregisterTrainSchedulesListener(listener: TrainManager.TrainSchedulesListener)

    interface TrainTrafficListener {
        fun onTrainTrafficChanged(@TrainTrafficType trainTrafficType: Long)
    }

    interface TrainSchedulesListener {
        fun onTrainSchedulesChanged(@TrainSchedulesType trainSchedulesType: Long)
    }


}