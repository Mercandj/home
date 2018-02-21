package com.mercandalli.core.gitlab

import com.mercandalli.core.main_thread.MainThreadPost
import okhttp3.OkHttpClient

class GitLabModule(
        private val okHttpClient: OkHttpClient,
        private val mainThreadPost: MainThreadPost) {

    fun provideGitLabManager(): GitLabManager {
        return GitLabManagerImpl(
                provideGitLabApi(),
                provideGitLabProjectParser(),
                provideGitLabBuildParser(),
                mainThreadPost)
    }

    private fun provideGitLabApi(): GitLabApi {
        return GitLabApiOkHttp(okHttpClient)
    }

    private fun provideGitLabProjectParser(): GitLabProjectParser {
        return GitLabProjectParserImpl()
    }

    private fun provideGitLabBuildParser(): GitLabBuildParser {
        return GitLabBuildParserImpl()
    }
}
