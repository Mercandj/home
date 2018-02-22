package com.mercandalli.core_ui.gitlab

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.mercandalli.core.gitlab.GitLabManager
import com.mercandalli.core.gitlab.GitLabProject
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core_ui.R

class GitLabProjectsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val refreshRate = 20_000L
    private val gitLabManager: GitLabManager
    private val gitLabProjectListener = createGitLabProjectListener()
    private val swipeRefreshLayout: SwipeRefreshLayout
    private val recyclerView: RecyclerView
    private val adapter: GitLabAdapter

    private val syncRunnable = Runnable {
        syncRequest()
        postSync()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_git_lab_projects, this)
        gitLabManager = CoreGraph.get().provideGitLabManager()
        adapter = GitLabAdapter(createOnGitLabProjectClickListener())
        swipeRefreshLayout = findViewById(R.id.view_git_lab_projects_swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            syncRequest()
        }
        recyclerView = findViewById(R.id.view_git_lab_projects_recycler_view)
        recyclerView.layoutManager = createLayoutManager()
        recyclerView.adapter = adapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        gitLabManager.registerGitLabProjectListener(gitLabProjectListener)
        syncRequest()
        syncView()
        postSync()
    }

    override fun onDetachedFromWindow() {
        gitLabManager.unregisterGitLabProjectListener(gitLabProjectListener)
        removeCallbacks(syncRunnable)
        super.onDetachedFromWindow()
    }

    private fun postSync() {
        removeCallbacks(syncRunnable)
        postDelayed(syncRunnable, refreshRate)
    }

    private fun syncView() {
        val gitLabProjects = gitLabManager.getGitLabProjects()
        adapter.setGitLabProjects(gitLabProjects)
    }

    private fun syncRequest() {
        val sync = gitLabManager.sync()
        swipeRefreshLayout.isRefreshing = sync
    }

    private fun createGitLabProjectListener(): GitLabManager.GitLabProjectListener {
        return object : GitLabManager.GitLabProjectListener {
            override fun onGitLabProjectChanged() {
                syncView()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun createOnGitLabProjectClickListener(): GitLabProjectCardView.OnGitLabProjectClickListener {
        return object : GitLabProjectCardView.OnGitLabProjectClickListener {
            override fun onGitLabProjectClicked(gitLabProject: GitLabProject) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(gitLabProject.webUrl)
                context.startActivity(intent)
            }
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(
                context.resources.getInteger(R.integer.grid_nb_column),
                StaggeredGridLayoutManager.VERTICAL)
    }
}
