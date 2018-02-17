package com.mercandalli.core.train

interface TrainApi {

    fun getTrainTraffic(
            @TrainManager.Companion.TrainTrafficType trainTrafficType: Long): TrainTraffic?

    fun getTrainSchedules(
            @TrainManager.Companion.TrainSchedulesType trainSchedulesType: Long): TrainSchedules?

}
