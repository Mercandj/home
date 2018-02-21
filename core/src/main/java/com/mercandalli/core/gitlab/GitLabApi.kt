package com.mercandalli.core.gitlab

interface GitLabApi {

    fun getGitLabProject(): String?

    fun getGitLabBuild(projectId: Int): String?
}
