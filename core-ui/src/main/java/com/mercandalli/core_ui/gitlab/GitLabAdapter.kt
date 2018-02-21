package com.mercandalli.core_ui.gitlab

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.core.gitlab.GitLabProject
import com.mercandalli.core_ui.R

internal class GitLabAdapter constructor(
        onPlaylistClickListener: GitLabProjectCardView.OnGitLabProjectClickListener)
    : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(GitLabProjectAdapterDelegate(onPlaylistClickListener)
                as AdapterDelegate<List<Any>>)
    }

    fun setGitLabProjects(gitLabProjects: List<GitLabProject>) {
        val list = ArrayList<Any>(gitLabProjects)
        setItems(list)
        notifyDataSetChanged()
    }

    //region GitLabProject
    private class GitLabProjectAdapterDelegate constructor(
            private val onPlaylistClickListener: GitLabProjectCardView.OnGitLabProjectClickListener) :
            AbsListItemAdapterDelegate<Any, Any, GitLabProjectViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is GitLabProject
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): GitLabProjectViewHolder {
            val gitLabProjectCardView = GitLabProjectCardView(viewGroup.context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.bottomMargin = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginStart = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
            layoutParams.marginEnd = viewGroup.context.resources.getDimensionPixelSize(R.dimen.default_space_half)
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
}