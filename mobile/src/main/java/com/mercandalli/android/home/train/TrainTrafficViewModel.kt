package com.mercandalli.android.home.train

import com.mercandalli.core.train.TrainManager
import com.mercandalli.core.train.TrainTraffic

data class TrainTrafficViewModel(
        val title: String,
        @TrainManager.Companion.TrainTrafficType val trainTrafficType: Long,
        val trainTraffic: TrainTraffic?)
