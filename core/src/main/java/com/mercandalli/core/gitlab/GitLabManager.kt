package com.mercandalli.core.gitlab

interface GitLabManager {

    fun sync(): Boolean

    fun getGitLabProjects(): List<GitLabProject>

    fun getGitLabBuild(gitLabProjectId: Int): List<GitLabBuild>

    fun registerGitLabProjectListener(listener: GitLabProjectListener)

    fun unregisterGitLabProjectListener(listener: GitLabProjectListener)

    interface GitLabProjectListener {
        fun onGitLabProjectChanged()
    }

}
