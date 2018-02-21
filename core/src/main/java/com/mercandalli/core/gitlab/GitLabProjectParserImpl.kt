package com.mercandalli.core.gitlab

import org.json.JSONArray

class GitLabProjectParserImpl : GitLabProjectParser {

    override fun parse(json: String?): List<GitLabProject> {
        val result = ArrayList<GitLabProject>()
        if (json == null) {
            return result
        }
        val projectsJson = JSONArray(json)
        (0 until projectsJson.length())
                .map { projectsJson.getJSONObject(it) }
                .mapTo(result) { GitLabProject.fromJson(it) }
        return result
    }

}
