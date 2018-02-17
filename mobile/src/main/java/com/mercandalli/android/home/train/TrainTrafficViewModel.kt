package com.mercandalli.android.home.train

import com.mercandalli.core.train.TrainTraffic

data class TrainTrafficViewModel(
        private val title: String,
        private val trainTraffic: TrainTraffic?)
