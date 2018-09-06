package com.mercandalli.core.gitlab

import com.mercandalli.core.gitlab.GitLabConst.Companion.GITLAB_DOMAIN
import com.mercandalli.core.main.Closer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class GitLabApiOkHttp(
        private val okHttpClient: OkHttpClient
) : GitLabApi {

    companion object {
        private const val API_VERSION = "v4"
        val URL_FETCH_PROJECTS = GITLAB_DOMAIN + "api/$API_VERSION/projects/?per_page=60&order_by=last_activity_at"
        val URL_FETCH_BUILDS = GITLAB_DOMAIN + "api/$API_VERSION/projects/%projectId/jobs"
    }

    override fun getGitLabProject(): String? {
        val request = Request.Builder()
                .url(URL_FETCH_PROJECTS)
                .header("PRIVATE-TOKEN", GitLabConst.GITLAB_PRIVATE_TOKEN)
                .build()
        var response: Response? = null
        var body: ResponseBody? = null
        try {
            response = okHttpClient.newCall(request).execute()
            body = response!!.body()
            return body!!.string()
        } catch (ignored: IOException) {
        } finally {
            Closer.closeSilently(body, response)
        }
        return null
    }

    override fun getGitLabBuild(projectId: Int): String? {
        val request = Request.Builder()
                .url(getUrlFetchBuilds(projectId))
                .header("PRIVATE-TOKEN", GitLabConst.GITLAB_PRIVATE_TOKEN)
                .build()
        var response: Response? = null
        var body: ResponseBody? = null
        try {
            response = okHttpClient.newCall(request).execute()
            body = response!!.body()
            return body!!.string()
        } catch (ignored: IOException) {
        } finally {
            Closer.closeSilently(body, response)
        }
        return null
    }

    private fun getUrlFetchBuilds(projectId: Int): String {
        return URL_FETCH_BUILDS.replace("%projectId", projectId.toString())
    }
}
