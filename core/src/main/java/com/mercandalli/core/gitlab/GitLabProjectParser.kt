package com.mercandalli.core.gitlab

interface GitLabProjectParser {

    fun parse(json: String?): List<GitLabProject>

}
