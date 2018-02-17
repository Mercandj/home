package com.mercandalli.android.home.main

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.android.home.R
import com.mercandalli.android.home.train.TrainTrafficCardView
import com.mercandalli.core.train.TrainTraffic

internal class MainAdapter constructor(
        onTrainTrafficClickListener: TrainTrafficCardView.OnTrainTrafficClickListener)
    : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(TrainTrafficAdapterDelegate(onTrainTrafficClickListener)
                as AdapterDelegate<List<Any>>)
    }

    fun setTrainTraffic(trainTraffic: List<TrainTraffic>) {
        val list = ArrayList<Any>(trainTraffic)
        setItems(list)
        notifyDataSetChanged()
    }

    //region TrainTraffic
    private class TrainTrafficAdapterDelegate constructor(
            private val onPlaylistClickListener: TrainTrafficCardView.OnTrainTrafficClickListener) :
            AbsListItemAdapterDelegate<Any, Any, TrainTrafficViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is TrainTraffic
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
            gitLabProjectViewHolder.bind(model as TrainTraffic)
        }
    }

    private class TrainTrafficViewHolder(
            private val view: TrainTrafficCardView) :
            RecyclerView.ViewHolder(view) {
        fun bind(trainTraffic: TrainTraffic) {
            view.setTrainTraffic(trainTraffic)
        }
    }
    //endregion TrainTraffic
}