package com.mercandalli.core.gitlab

interface GitLabBuildParser {

    fun parse(json: String?, limit:Int): List<GitLabBuild>

}
