package com.mercandalli.core.train

import com.mercandalli.core.main_thread.MainThreadPost
import kotlinx.coroutines.experimental.async

class TrainManagerImpl(
        private val trainApi: TrainApi,
        private val mainThreadPost: MainThreadPost) : TrainManager {

    private val trainSyncListeners = ArrayList<TrainManager.TrainSyncListener>()
    private val trainTraffics = HashMap<Int, TrainTraffic?>()
    private val trainTrafficListeners = ArrayList<TrainManager.TrainTrafficListener>()
    private val trainSchedules = HashMap<Int, TrainSchedules?>()
    private val trainSchedulesListeners = ArrayList<TrainManager.TrainSchedulesListener>()

    override fun synchroniseAsync() {
        async {
            notifyTrainTrafficListener(TrainManager.TRAFFIC_A, trainApi.getTrainTraffic(TrainManager.TRAFFIC_A))
            notifyTrainTrafficListener(TrainManager.TRAFFIC_D, trainApi.getTrainTraffic(TrainManager.TRAFFIC_D))
            notifyTrainTrafficListener(TrainManager.TRAFFIC_9, trainApi.getTrainTraffic(TrainManager.TRAFFIC_9))
            notifyTrainTrafficListener(TrainManager.TRAFFIC_14, trainApi.getTrainTraffic(TrainManager.TRAFFIC_14))
            notifyTrainSchedulesListener(TrainManager.SCHEDULES_GARE_DE_LYON_A, trainApi.getTrainSchedules(TrainManager.SCHEDULES_GARE_DE_LYON_A))
            notifyTrainSchedulesListener(TrainManager.SCHEDULES_BOISSY_A, trainApi.getTrainSchedules(TrainManager.SCHEDULES_BOISSY_A))
            notifyTrainSchedulesListener(TrainManager.SCHEDULES_YERRES_D, trainApi.getTrainSchedules(TrainManager.SCHEDULES_YERRES_D))
            notifyTrainSchedulesListener(TrainManager.SCHEDULES_GARE_DE_LYON_D, trainApi.getTrainSchedules(TrainManager.SCHEDULES_GARE_DE_LYON_D))
            notifyTrainSyncListener()
        }
    }

    override fun trainTrafficSync(@TrainManager.Companion.TrainTrafficType trainTrafficType: Int): TrainTraffic? {
        return trainApi.getTrainTraffic(trainTrafficType)
    }

    override fun trainTrafficSchedules(trainSchedulesType: Int): TrainSchedules? {
        return trainApi.getTrainSchedules(trainSchedulesType)
    }

    override fun registerTrainSyncListener(listener: TrainManager.TrainSyncListener) {
        if (trainSyncListeners.contains(listener)) {
            return
        }
        trainSyncListeners.add(listener)
    }

    override fun unregisterTrainSyncListener(listener: TrainManager.TrainSyncListener) {
        trainSyncListeners.remove(listener)
    }

    override fun getTrainTraffic(trainTrafficType: Int): TrainTraffic? {
        return trainTraffics[trainTrafficType]
    }

    override fun registerTrainTrafficListener(listener: TrainManager.TrainTrafficListener) {
        if (trainTrafficListeners.contains(listener)) {
            return
        }
        trainTrafficListeners.add(listener)
    }

    override fun unregisterTrainTrafficListener(listener: TrainManager.TrainTrafficListener) {
        trainTrafficListeners.remove(listener)
    }

    override fun getTrainSchedules(trainSchedulesType: Int): TrainSchedules? {
        return trainSchedules[trainSchedulesType]
    }

    override fun registerTrainSchedulesListener(listener: TrainManager.TrainSchedulesListener) {
        if (trainSchedulesListeners.contains(listener)) {
            return
        }
        trainSchedulesListeners.add(listener)
    }

    override fun unregisterTrainSchedulesListener(listener: TrainManager.TrainSchedulesListener) {
        trainSchedulesListeners.remove(listener)
    }

    private fun notifyTrainSyncListener() {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyTrainSyncListener() })
            return
        }
        for (listener in trainSyncListeners) {
            listener.onTrainSyncFinished()
        }
    }

    private fun notifyTrainTrafficListener(
            @TrainManager.Companion.TrainTrafficType trainTrafficType: Int,
            trainTraffic: TrainTraffic?) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyTrainTrafficListener(trainTrafficType, trainTraffic) })
            return
        }
        trainTraffics[trainTrafficType] = trainTraffic
        for (listener in trainTrafficListeners) {
            listener.onTrainTrafficChanged(trainTrafficType)
        }
    }

    private fun notifyTrainSchedulesListener(
            @TrainManager.Companion.TrainSchedulesType trainSchedulesType: Int,
            trainSchedules: TrainSchedules?) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyTrainSchedulesListener(trainSchedulesType, trainSchedules) })
            return
        }
        this.trainSchedules[trainSchedulesType] = trainSchedules
        for (listener in trainSchedulesListeners) {
            listener.onTrainSchedulesChanged(trainSchedulesType)
        }
    }

}