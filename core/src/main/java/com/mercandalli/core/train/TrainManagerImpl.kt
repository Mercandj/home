package com.mercandalli.core.train

import com.mercandalli.core.main_thread.MainThreadPost
import kotlinx.coroutines.experimental.async

class TrainManagerImpl(
        private val trainApi: TrainApi,
        private val mainThreadPost: MainThreadPost) : TrainManager {

    private val trainTraffics = HashMap<Long, TrainTraffic?>()
    private val listeners = ArrayList<TrainManager.TrainTrafficListener>()

    override fun sync() {
        async {
            val trainTraffic = trainApi.getTrainTraffic(TrainManager.TRAFFIC_A)
            notifyListener(TrainManager.TRAFFIC_A, trainTraffic)
        }
    }

    override fun getTrainTraffic(trainTrafficType: Long): TrainTraffic? {
        return trainTraffics[trainTrafficType]
    }

    override fun registerTrainTrafficListener(listener: TrainManager.TrainTrafficListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterTrainTrafficListener(listener: TrainManager.TrainTrafficListener) {
        listeners.remove(listener)
    }

    private fun notifyListener(
            @TrainManager.Companion.TrainTrafficType trainTrafficType: Long,
            trainTraffic: TrainTraffic?) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable { notifyListener(trainTrafficType, trainTraffic) })
            return
        }
        trainTraffics[trainTrafficType] = trainTraffic
        for (listener in listeners) {
            listener.onTrainTrafficChanged(trainTrafficType)
        }
    }

}