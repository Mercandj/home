package com.mercandalli.core.train

import com.mercandalli.core.main_thread.MainThreadPost
import kotlinx.coroutines.experimental.async

class TrainManagerImpl(
        private val trainApi: TrainApi,
        private val mainThreadPost: MainThreadPost) : TrainManager {

    private val trainTraffics = HashMap<Long, TrainTraffic?>()
    private val trainTrafficListeners = ArrayList<TrainManager.TrainTrafficListener>()
    private val trainSchedules = HashMap<Long, TrainSchedules?>()
    private val trainSchedulesListeners = ArrayList<TrainManager.TrainSchedulesListener>()

    override fun sync() {
        async {
            notifyTrainTrafficListener(TrainManager.TRAFFIC_A, trainApi.getTrainTraffic(TrainManager.TRAFFIC_A))
            notifyTrainTrafficListener(TrainManager.TRAFFIC_D, trainApi.getTrainTraffic(TrainManager.TRAFFIC_D))
            notifyTrainTrafficListener(TrainManager.TRAFFIC_9, trainApi.getTrainTraffic(TrainManager.TRAFFIC_9))
            notifyTrainTrafficListener(TrainManager.TRAFFIC_14, trainApi.getTrainTraffic(TrainManager.TRAFFIC_14))
            notifyTrainSchedulesListener(TrainManager.SCHEDULES_GARE_DE_LYON_A, trainApi.getTrainSchedules(TrainManager.SCHEDULES_GARE_DE_LYON_A))
            notifyTrainSchedulesListener(TrainManager.SCHEDULES_BOISSY_A, trainApi.getTrainSchedules(TrainManager.SCHEDULES_BOISSY_A))
        }
    }

    override fun getTrainTraffic(trainTrafficType: Long): TrainTraffic? {
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

    override fun getTrainSchedules(trainSchedulesType: Long): TrainSchedules? {
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

    private fun notifyTrainTrafficListener(
            @TrainManager.Companion.TrainTrafficType trainTrafficType: Long,
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
            @TrainManager.Companion.TrainSchedulesType trainSchedulesType: Long,
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