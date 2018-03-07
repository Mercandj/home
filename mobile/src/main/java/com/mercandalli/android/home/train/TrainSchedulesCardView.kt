package com.mercandalli.android.home.train

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.mercandalli.android.home.R
import com.mercandalli.android.home.main.MainAdapter
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core.train.TrainManager

class TrainSchedulesCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val title: TextView
    private val message: TextView
    private val trainManager: TrainManager
    @TrainManager.Companion.TrainSchedulesType
    private var trainSchedulesType: Int = 0
    private var trainSchedulesListener: TrainManager.TrainSchedulesListener

    init {
        LayoutInflater.from(context).inflate(R.layout.view_train_schedules, this)
        title = findViewById(R.id.view_train_schedules_title)
        message = findViewById(R.id.view_train_schedules_message)
        trainManager = CoreGraph.get().provideTrainManager()
        trainSchedulesListener = createTrainSchedulesListener()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        trainManager.registerTrainSchedulesListener(trainSchedulesListener)
        sync()
    }

    override fun onDetachedFromWindow() {
        trainManager.unregisterTrainSchedulesListener(trainSchedulesListener)
        super.onDetachedFromWindow()
    }

    fun setOnTrainTrafficClickListener(onPlaylistClickListener: OnTrainTrafficClickListener) {

    }

    fun setTrainSchedulesViewModel(trainSchedulesViewModel: MainAdapter.TrainSchedulesViewModel) {
        trainSchedulesType = trainSchedulesViewModel.trainSchedulesType
        title.text = trainSchedulesViewModel.title
        sync()
    }

    @SuppressLint("SetTextI18n")
    private fun sync() {
        val trainSchedules = trainManager.getTrainSchedules(trainSchedulesType)
        if (trainSchedules != null) {
            val schedules = trainSchedules.schedules
            message.text = ""
            for (i in 0 until schedules.size) {
                val trainSchedule = trainSchedules.schedules[i]
                message.text = message.text.toString() +
                        (if (i == 0) "" else "\n\n") +
                        trainSchedule.code + " - " + trainSchedule.message + "\n" +
                        trainSchedule.destination
            }
        } else {

        }
    }

    private fun createTrainSchedulesListener(): TrainManager.TrainSchedulesListener {
        return object : TrainManager.TrainSchedulesListener {
            override fun onTrainSchedulesChanged(trainSchedulesType: Int) {
                sync()
            }
        }
    }

    interface OnTrainTrafficClickListener {
        fun onTrainTrafficClicked()
    }
}
