package com.mercandalli.android.home.train

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.mercandalli.android.home.R
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core.train.TrainManager

class TrainTrafficCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val trainManager: TrainManager
    private val title: TextView
    private val message: TextView
    @TrainManager.Companion.TrainTrafficType
    private var trainTrafficType: Long = 0
    private var trainTrafficListener: TrainManager.TrainTrafficListener

    init {
        LayoutInflater.from(context).inflate(R.layout.view_train_traffic, this)
        title = findViewById(R.id.view_train_traffic_title)
        message = findViewById(R.id.view_train_traffic_message)
        trainManager = CoreGraph.get().provideTrainManager()
        trainTrafficListener = createTrainTrafficListener()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        trainManager.registerTrainTrafficListener(trainTrafficListener)
        sync()
    }

    override fun onDetachedFromWindow() {
        trainManager.unregisterTrainTrafficListener(trainTrafficListener)
        super.onDetachedFromWindow()
    }

    fun setOnTrainTrafficClickListener(onPlaylistClickListener: OnTrainTrafficClickListener) {

    }

    fun setTrainTrafficViewModel(trainTrafficViewModel: TrainTrafficViewModel) {
        title.text = trainTrafficViewModel.title
        trainTrafficType = trainTrafficViewModel.trainTrafficType
        sync()
    }

    private fun sync() {
        val trainTraffic = trainManager.getTrainTraffic(trainTrafficType)
        if (trainTraffic != null) {
            message.visibility = View.VISIBLE
            message.text = trainTraffic.message
        } else {
            message.visibility = View.GONE
        }
    }

    private fun createTrainTrafficListener(): TrainManager.TrainTrafficListener {
        return object : TrainManager.TrainTrafficListener {
            override fun onTrainTrafficChanged(trainTrafficType: Long) {

            }
        }
    }

    interface OnTrainTrafficClickListener {
        fun onTrainTrafficClicked()
    }
}
