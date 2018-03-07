package com.mercandalli.android.home.main

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.android.home.R
import com.mercandalli.android.home.gpio.GpioCardView
import com.mercandalli.android.home.train.TrainSchedulesCardView
import com.mercandalli.android.home.train.TrainTrafficCardView
import com.mercandalli.core.gitlab.GitLabProject
import com.mercandalli.core.train.TrainManager
import com.mercandalli.core.train.TrainTraffic
import com.mercandalli.core_ui.gitlab.GitLabProjectCardView

class MainAdapter constructor(
        onTrainTrafficClickListener: TrainTrafficCardView.OnTrainTrafficClickListener,
        onPlaylistClickListener: GitLabProjectCardView.OnGitLabProjectClickListener)
    : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(SectionTitleAdapterDelegate())
        delegatesManager.addDelegate(GitLabProjectAdapterDelegate(onPlaylistClickListener)
                as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(GpioAdapterDelegate())
        delegatesManager.addDelegate(TrainTrafficAdapterDelegate(onTrainTrafficClickListener)
                as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(TrainSchedulesAdapterDelegate())
    }

    fun setViewModel(
            trainTrafficViewModels: List<TrainTrafficViewModel>,
            trainSchedulesViewModels: List<TrainSchedulesViewModel>,
            gitLabProjects: List<GitLabProject>) {
        val list = ArrayList<Any>()
        list.add(SectionTitleViewModel("Gpio"))
        list.add(GpioViewModel("Android things"))
        list.add(SectionTitleViewModel("Traffic"))
        list.addAll(trainTrafficViewModels)
        list.add(SectionTitleViewModel("Schedules"))
        list.addAll(trainSchedulesViewModels)
        list.add(SectionTitleViewModel("GitLab"))
        list.addAll(gitLabProjects)
        setItems(list)
        notifyDataSetChanged()
    }

    //region SectionTitle
    private class SectionTitleAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, SectionTitleViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is SectionTitleViewModel
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): SectionTitleViewHolder {
            val view = SectionTitleCardView(viewGroup.context)
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.bottomMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginStart = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginEnd = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.isFullSpan = true
            view.layoutParams = layoutParams
            return SectionTitleViewHolder(view)
        }

        override fun onBindViewHolder(
                model: Any, viewHolder: SectionTitleViewHolder, list: List<Any>) {
            viewHolder.bind(model as SectionTitleViewModel)
        }
    }

    private class SectionTitleViewHolder(
            private val view: SectionTitleCardView) :
            RecyclerView.ViewHolder(view) {
        fun bind(sectionTitleViewModel: SectionTitleViewModel) {
            view.text = sectionTitleViewModel.title
        }
    }

    private data class SectionTitleViewModel(val title: String)
    //endregion SectionTitle

    //region GitLabProject
    private class GitLabProjectAdapterDelegate constructor(
            private val onPlaylistClickListener: GitLabProjectCardView.OnGitLabProjectClickListener) :
            AbsListItemAdapterDelegate<Any, Any, GitLabProjectViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is GitLabProject
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): GitLabProjectViewHolder {
            val gitLabProjectCardView = GitLabProjectCardView(viewGroup.context)
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT,
                    StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = viewGroup.context.resources.getDimensionPixelSize(com.mercandalli.core_ui.R.dimen.default_space_half)
            layoutParams.bottomMargin = viewGroup.context.resources.getDimensionPixelSize(com.mercandalli.core_ui.R.dimen.default_space_half)
            layoutParams.marginStart = viewGroup.context.resources.getDimensionPixelSize(com.mercandalli.core_ui.R.dimen.default_space_half)
            layoutParams.marginEnd = viewGroup.context.resources.getDimensionPixelSize(com.mercandalli.core_ui.R.dimen.default_space_half)
            layoutParams.isFullSpan = true
            gitLabProjectCardView.layoutParams = layoutParams
            gitLabProjectCardView.setOnGitLabProjectClickListener(onPlaylistClickListener)
            return GitLabProjectViewHolder(gitLabProjectCardView)
        }

        override fun onBindViewHolder(
                model: Any, gitLabProjectViewHolder: GitLabProjectViewHolder, list: List<Any>) {
            gitLabProjectViewHolder.bind(model as GitLabProject)
        }
    }

    private class GitLabProjectViewHolder(
            private val view: GitLabProjectCardView) :
            RecyclerView.ViewHolder(view) {
        fun bind(gitLabProject: GitLabProject) {
            view.setGitLabProject(gitLabProject)
        }
    }
    //endregion GitLabProject

    //region Gpio
    private class GpioAdapterDelegate :
            AbsListItemAdapterDelegate<Any, Any, GpioViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is GpioViewModel
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): GpioViewHolder {
            val view = GpioCardView(viewGroup.context)
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.bottomMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginStart = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginEnd = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.isFullSpan = true
            view.layoutParams = layoutParams
            return GpioViewHolder(view)
        }

        override fun onBindViewHolder(
                model: Any, viewHolder: GpioViewHolder, list: List<Any>) {
            viewHolder.bind(model as GpioViewModel)
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
            val view = TrainTrafficCardView(viewGroup.context)
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.bottomMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginStart = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginEnd = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            view.layoutParams = layoutParams
            layoutParams.isFullSpan = false
            view.setOnTrainTrafficClickListener(onPlaylistClickListener)
            return TrainTrafficViewHolder(view)
        }

        override fun onBindViewHolder(
                model: Any, viewHolder: TrainTrafficViewHolder, list: List<Any>) {
            viewHolder.bind(model as TrainTrafficViewModel)
        }
    }

    private class TrainTrafficViewHolder(
            private val view: TrainTrafficCardView) :
            RecyclerView.ViewHolder(view) {
        fun bind(trainTrafficViewModel: TrainTrafficViewModel) {
            view.setTrainTrafficViewModel(trainTrafficViewModel)
        }
    }

    data class TrainTrafficViewModel(
            val title: String,
            @TrainManager.Companion.TrainTrafficType val trainTrafficType: Long,
            val trainTraffic: TrainTraffic?)
    //endregion TrainTraffic

    //region TrainSchedules
    private class TrainSchedulesAdapterDelegate : AbsListItemAdapterDelegate<Any, Any, TrainSchedulesViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is TrainSchedulesViewModel
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): TrainSchedulesViewHolder {
            val view = TrainSchedulesCardView(viewGroup.context)
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.bottomMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginStart = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginEnd = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            view.layoutParams = layoutParams
            layoutParams.isFullSpan = false
            return TrainSchedulesViewHolder(view)
        }

        override fun onBindViewHolder(
                model: Any, viewHolder: TrainSchedulesViewHolder, list: List<Any>) {
            viewHolder.bind(model as TrainSchedulesViewModel)
        }
    }

    private class TrainSchedulesViewHolder(
            private val view: TrainSchedulesCardView) :
            RecyclerView.ViewHolder(view) {
        fun bind(trainTrafficViewModel: TrainSchedulesViewModel) {
            view.setTrainSchedulesViewModel(trainTrafficViewModel)
        }
    }

    data class TrainSchedulesViewModel(
            val title: String,
            @TrainManager.Companion.TrainSchedulesType val trainSchedulesType: Int,
            val trainTraffic: TrainTraffic?)

    //endregion TrainSchedules
}