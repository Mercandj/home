package com.mercandalli.core.gitlab

import org.json.JSONObject

data class GitLabUser(
        val id: Int,
        val name: String,
        val avatarUrl: String) {

    companion object {
        @JvmStatic
        fun fromJson(json: JSONObject): GitLabUser {
            return GitLabUser(
                    json.getInt("id"),
                    json.getString("name"),
                    json.getString("avatar_url"))
        }
    }
}
