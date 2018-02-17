package com.mercandalli.android.home.train

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet

class TrainTrafficCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    fun setOnTrainTrafficClickListener(onPlaylistClickListener: OnTrainTrafficClickListener) {

    }

    fun setTrainTrafficViewModel(trainTraffic: TrainTrafficViewModel) {

    }

    interface OnTrainTrafficClickListener {
        fun OnTrainTrafficClicked()
    }
}
