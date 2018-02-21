package com.mercandalli.core.gitlab

import org.json.JSONObject

data class GitLabProject(
        val id: Int,
        val name: String,
        val webUrl: String,
        val avatarUrl: String,
        val owner: GitLabUser?,
        val lastActivityAt: String) {

    companion object {
        @JvmStatic
        fun fromJson(json: JSONObject): GitLabProject {
            return GitLabProject(
                    json.getInt("id"),
                    json.getString("name"),
                    json.getString("web_url"),
                    json.getString("avatar_url"),
                    if (json.has("owner")) GitLabUser.fromJson(json.getJSONObject("owner"))
                    else null,
                    json.getString("last_activity_at"))
        }
    }
}
