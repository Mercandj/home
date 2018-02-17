package com.mercandalli.android.home.main

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.android.home.R
import com.mercandalli.android.home.gpio.GpioCardView
import com.mercandalli.android.home.train.TrainTrafficCardView
import com.mercandalli.android.home.train.TrainTrafficViewModel

internal class MainAdapter constructor(
        onTrainTrafficClickListener: TrainTrafficCardView.OnTrainTrafficClickListener)
    : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(GpioAdapterDelegate())
        delegatesManager.addDelegate(TrainTrafficAdapterDelegate(onTrainTrafficClickListener)
                as AdapterDelegate<List<Any>>)
    }

    fun setTrainTrafficViewModel(trainTrafficViewModel: List<TrainTrafficViewModel>) {
        val list = ArrayList<Any>()
        list.add(GpioViewModel("Android things"))
        list.addAll(trainTrafficViewModel)
        setItems(list)
        notifyDataSetChanged()
    }

    //region Gpio
    private class GpioAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, GpioViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is GpioViewModel
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): GpioViewHolder {
            val gitLabProjectCardView = GpioCardView(viewGroup.context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.bottomMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginStart = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginEnd = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            gitLabProjectCardView.layoutParams = layoutParams
            return GpioViewHolder(gitLabProjectCardView)
        }

        override fun onBindViewHolder(
                model: Any, gitLabProjectViewHolder: GpioViewHolder, list: List<Any>) {
            gitLabProjectViewHolder.bind(model as GpioViewModel)
        }
    }

    private class GpioViewHolder(
            private val view: GpioCardView) :
            RecyclerView.ViewHolder(view) {
        fun bind(gpioViewModel: GpioViewModel) {
            view.setTitle(gpioViewModel.title)
        }
    }

    private data class GpioViewModel(val title: String)
    //endregion Gpio

    //region TrainTraffic
    private class TrainTrafficAdapterDelegate constructor(
            private val onPlaylistClickListener: TrainTrafficCardView.OnTrainTrafficClickListener) :
            AbsListItemAdapterDelegate<Any, Any, TrainTrafficViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is TrainTrafficViewModel
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): TrainTrafficViewHolder {
            val gitLabProjectCardView = TrainTrafficCardView(viewGroup.context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.bottomMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginStart = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginEnd = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            gitLabProjectCardView.layoutParams = layoutParams
            gitLabProjectCardView.setOnTrainTrafficClickListener(onPlaylistClickListener)
            return TrainTrafficViewHolder(gitLabProjectCardView)
        }

        override fun onBindViewHolder(
                model: Any, gitLabProjectViewHolder: TrainTrafficViewHolder, list: List<Any>) {
            gitLabProjectViewHolder.bind(model as TrainTrafficViewModel)
        }
    }

    private class TrainTrafficViewHolder(
            private val view: TrainTrafficCardView) :
            RecyclerView.ViewHolder(view) {
        fun bind(trainTrafficViewModel: TrainTrafficViewModel) {
            view.setTrainTrafficViewModel(trainTrafficViewModel)
        }
    }
    //endregion TrainTraffic
}