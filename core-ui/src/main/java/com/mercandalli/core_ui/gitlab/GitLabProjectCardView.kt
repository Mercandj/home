package com.mercandalli.core_ui.gitlab

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.mercandalli.core.gitlab.GitLabConst
import com.mercandalli.core.gitlab.GitLabManager
import com.mercandalli.core.gitlab.GitLabProject
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core_ui.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class GitLabProjectCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val gitLabManager: GitLabManager
    private val gitLabProjectListener = createGitLabProjectListener()
    private var gitLabProject: GitLabProject? = null
    private val name: TextView
    private val lastActivity: TextView
    private val build = ArrayList<GitLabBuildRow>(4)
    private var onPlaylistClickListener: OnGitLabProjectClickListener? = null
    private var projectImage: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_git_lab_project, this)
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.git_lab_project_card_bg))
        name = findViewById(R.id.view_git_lab_project_name)
        lastActivity = findViewById(R.id.view_git_lab_project_last_activity)
        projectImage = findViewById(R.id.view_git_lab_project_image)
        build.add(findViewById(R.id.view_git_lab_project_build_1))
        build.add(findViewById(R.id.view_git_lab_project_build_2))
        build.add(findViewById(R.id.view_git_lab_project_build_3))
        build.add(findViewById(R.id.view_git_lab_project_build_4))
        gitLabManager = CoreGraph.get().gitLabManager
        name.setOnClickListener {
            onPlaylistClickListener!!.onGitLabProjectClicked(gitLabProject!!)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        gitLabManager.registerGitLabProjectListener(gitLabProjectListener)
        syncView()
    }

    override fun onDetachedFromWindow() {
        gitLabManager.unregisterGitLabProjectListener(gitLabProjectListener)
        super.onDetachedFromWindow()
    }

    fun setOnGitLabProjectClickListener(onPlaylistClickListener: OnGitLabProjectClickListener) {
        this.onPlaylistClickListener = onPlaylistClickListener
    }

    fun setGitLabProject(gitLabProject: GitLabProject) {
        this.gitLabProject = gitLabProject
        syncView()
    }

    private fun getUrlWithHeaders(url: String): GlideUrl {
        return GlideUrl(url, LazyHeaders.Builder()
                .addHeader("Private-Token", GitLabConst.GITLAB_PRIVATE_TOKEN)
                .build())
    }

    private fun syncView() {
        if (gitLabProject == null) {
            name.text = "Error"
            return
        }
        name.text = gitLabProject!!.name
        val gitLabBuilds = gitLabManager.getGitLabBuild(gitLabProject!!.id)
        val size = gitLabBuilds.size
        for (i in 0 until build.size) {
            if (i < size) {
                val gitLabBuild = gitLabBuilds[i]
                build[i].visibility = View.VISIBLE
                build[i].setGitLabBuild(gitLabBuild)
            } else {
                build[i].visibility = View.GONE
            }
        }
        if (gitLabProject!!.avatarUrl == "null") {
            projectImage.visibility = View.GONE
        } else {
            projectImage.visibility = View.VISIBLE
            GlideApp.with(context)
                    .load(getUrlWithHeaders(gitLabProject!!.avatarUrl))
                    .centerCrop()
                    .into(projectImage)
        }
        lastActivity.text = toDateAgo(gitLabProject!!.lastActivityAt)
    }

    private fun createGitLabProjectListener(): GitLabManager.GitLabProjectListener {
        return object : GitLabManager.GitLabProjectListener {
            override fun onGitLabProjectChanged() {
                syncView()
            }
        }
    }

    private fun toDateAgo(dateStringInput: String): String {
        if (dateStringInput == "null" || dateStringInput == "") {
            return ""
        }
        val past = format.parse(dateStringInput)
        val now = Date()
        val timeDifferenceMS = (now.time - past.time) - TimeUnit.HOURS.toMillis(1)
        val hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMS)
        if (hours > 48) {
            return (hours / 24).toString() + " days ago"
        }
        if (hours > 24) {
            return "1 day ago"
        }
        if (hours > 1) {
            return hours.toString() + " hours ago"
        }
        if (hours > 0) {
            return "1 hour ago"
        }
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMS)
        if (minutes > 1) {
            return minutes.toString() + " minutes ago"
        }
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMS)
        if (seconds > 0) {
            return seconds.toString() + " seconds ago"
        }
        return "now"
    }

    interface OnGitLabProjectClickListener {
        fun onGitLabProjectClicked(gitLabProject: GitLabProject)
    }

    companion object {
        var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    }
}
