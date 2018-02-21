package com.mercandalli.core.gitlab

import io.wax911.emojify.EmojiUtils
import org.json.JSONObject

data class GitLabCommit(
        val id: String,
        val title: String,
        val authorName: String) {

    companion object {
        private fun emojifyIfNeeded(input: String): String {
            if (!input.contains(':')) {
                return input
            }
            return EmojiUtils.emojify(input)
        }

        fun fromJson(json: JSONObject): GitLabCommit {
            var commitTitle = json.getString("title")
            if (commitTitle.contains(':')) {
                commitTitle = emojifyIfNeeded(commitTitle)
            }
            return GitLabCommit(
                    json.getString("id"),
                    commitTitle,
                    json.getString("author_name"))
        }
    }

}
