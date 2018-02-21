package com.mercandalli.core.gitlab

import org.json.JSONArray

class GitLabBuildParserImpl : GitLabBuildParser {

    override fun parse(json: String?, limit: Int): List<GitLabBuild> {
        val result = ArrayList<GitLabBuild>()
        if (json == null) {
            return result
        }
        val buildsJson = JSONArray(json)

        var lastCommitTitle = ""
        for (i in 0 until buildsJson.length()) {
            val buildJson = buildsJson.getJSONObject(i)
            val build = GitLabBuild.fromJson(buildJson)
            if (lastCommitTitle != build.commit.title) {
                result.add(build)
            }
            lastCommitTitle = build.commit.title
            if (result.size >= limit) {
                return result
            }
        }
        return result
    }

}
