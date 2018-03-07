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

        const val TRAFFIC_A = 0
        const val TRAFFIC_D = 1
        const val TRAFFIC_9 = 2
        const val TRAFFIC_14 = 3

        @IntDef(
                SCHEDULES_GARE_DE_LYON_A,
                SCHEDULES_BOISSY_A,
                SCHEDULES_YERRES_D,
                SCHEDULES_GARE_DE_LYON_D)
        @Retention(AnnotationRetention.SOURCE)
        annotation class TrainSchedulesType

        const val SCHEDULES_GARE_DE_LYON_A = 0
        const val SCHEDULES_BOISSY_A = 1
        const val SCHEDULES_YERRES_D = 2
        const val SCHEDULES_GARE_DE_LYON_D = 3
    }

    fun synchroniseAsync()

    fun trainTrafficSync(@TrainManager.Companion.TrainTrafficType trainTrafficType: Int): TrainTraffic?

    fun trainTrafficSchedules(@TrainManager.Companion.TrainSchedulesType trainSchedulesType: Int): TrainSchedules?

    fun registerTrainSyncListener(listener: TrainManager.TrainSyncListener)

    fun unregisterTrainSyncListener(listener: TrainManager.TrainSyncListener)

    fun getTrainTraffic(@TrainTrafficType trainTrafficType: Int): TrainTraffic?

    fun registerTrainTrafficListener(listener: TrainManager.TrainTrafficListener)

    fun unregisterTrainTrafficListener(listener: TrainManager.TrainTrafficListener)

    fun getTrainSchedules(@TrainSchedulesType trainSchedulesType: Int): TrainSchedules?

    fun registerTrainSchedulesListener(listener: TrainManager.TrainSchedulesListener)

    fun unregisterTrainSchedulesListener(listener: TrainManager.TrainSchedulesListener)

    interface TrainSyncListener {
        fun onTrainSyncFinished()
    }

    interface TrainTrafficListener {
        fun onTrainTrafficChanged(@TrainTrafficType trainTrafficType: Int)
    }

    interface TrainSchedulesListener {
        fun onTrainSchedulesChanged(@TrainSchedulesType trainSchedulesType: Int)
    }


}