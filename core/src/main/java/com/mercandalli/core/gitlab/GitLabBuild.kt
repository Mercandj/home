package com.mercandalli.core.gitlab

import org.json.JSONObject

data class GitLabBuild(
        val id: Int,
        val commit: GitLabCommit,
        val pipelineStatus: String,
        val userAvatarUrl: String) {

    companion object {
        fun fromJson(json: JSONObject): GitLabBuild {
            return GitLabBuild(
                    json.getInt("id"),
                    GitLabCommit.fromJson(json.getJSONObject("commit")),
                    if (json.has("pipeline")) json.getJSONObject("pipeline").getString("status")
                    else "",
                    if (json.has("user")) json.getJSONObject("user").getString("avatar_url")
                    else "")
        }
    }

}
