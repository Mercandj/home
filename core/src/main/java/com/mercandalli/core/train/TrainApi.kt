package com.mercandalli.core.train

interface TrainApi {

    fun getTrainTraffic(
            @TrainManager.Companion.TrainTrafficType trainTrafficType: Int): TrainTraffic?

    fun getTrainSchedules(
            @TrainManager.Companion.TrainSchedulesType trainSchedulesType: Int): TrainSchedules?

}
