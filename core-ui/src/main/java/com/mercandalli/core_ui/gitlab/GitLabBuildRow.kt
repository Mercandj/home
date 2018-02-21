package com.mercandalli.core_ui.gitlab

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.mercandalli.core.gitlab.GitLabBuild
import com.mercandalli.core.gitlab.GitLabConst
import com.mercandalli.core_ui.R

class GitLabBuildRow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val author: TextView
    private val authorImage: ImageView
    private val buildCommitTitle: TextView
    private val buildPipelineStatus: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_git_lab_build_row, this)
        author = findViewById(R.id.view_git_lab_project_build_author)
        authorImage = findViewById(R.id.view_git_lab_project_build_author_image)
        buildCommitTitle = findViewById(R.id.view_git_lab_project_build_commit_title)
        buildPipelineStatus = findViewById(R.id.view_git_lab_project_build_pipeline_status)
    }

    fun setGitLabBuild(gitLabBuild: GitLabBuild) {
        val commit = gitLabBuild.commit
        buildCommitTitle.text = commit.title
        if (gitLabBuild.pipelineStatus == "success") {
            buildPipelineStatus.visibility = View.VISIBLE
            buildPipelineStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green_24dp))
        } else if (gitLabBuild.pipelineStatus == "failed") {
            buildPipelineStatus.visibility = View.VISIBLE
            buildPipelineStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_remove_circle_red_24dp))
        } else if (gitLabBuild.pipelineStatus == "canceled" || gitLabBuild.pipelineStatus == "skipped") {
            buildPipelineStatus.visibility = View.VISIBLE
            buildPipelineStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cancel_yellow_24dp))
        } else if (gitLabBuild.pipelineStatus == "pending" || gitLabBuild.pipelineStatus == "running") {
            buildPipelineStatus.visibility = View.VISIBLE
            buildPipelineStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sync_blue_24dp))
        } else {
            buildPipelineStatus.visibility = View.GONE
        }
        author.text = commit.authorName

        if (gitLabBuild.userAvatarUrl == "null") {
            authorImage.visibility = View.INVISIBLE
        } else {
            authorImage.visibility = View.VISIBLE
            GlideApp.with(context)
                    .load(getUrlWithHeaders(gitLabBuild.userAvatarUrl))
                    .centerCrop()
                    .into(authorImage)
        }
    }

    private fun getUrlWithHeaders(url: String): GlideUrl {
        return GlideUrl(url, LazyHeaders.Builder()
                .addHeader("Private-Token", GitLabConst.GITLAB_PRIVATE_TOKEN)
                .build())
    }
}
