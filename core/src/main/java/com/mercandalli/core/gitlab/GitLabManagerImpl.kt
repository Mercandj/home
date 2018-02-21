package com.mercandalli.core.gitlab

import com.mercandalli.core.main_thread.MainThreadPost
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

/* package */ class GitLabManagerImpl(
        private val gitLabApi: GitLabApi,
        private val gitLabProjectParser: GitLabProjectParser,
        private val gitLabBuildParser: GitLabBuildParser,
        private val mainThreadPost: MainThreadPost) : GitLabManager {

    private val gitLabProjects = ArrayList<GitLabProject>()
    private val gitLabBuilds = HashMap<Int, List<GitLabBuild>>()
    private val listeners = ArrayList<GitLabManager.GitLabProjectListener>()
    private var isSyncing = false

    override fun sync(): Boolean {
        if (isSyncing) {
            return false
        }
        isSyncing = true
        async(CommonPool) { syncSync() }
        return true
    }

    override fun getGitLabProjects(): List<GitLabProject> {
        return ArrayList<GitLabProject>(gitLabProjects)
    }

    override fun getGitLabBuild(gitLabProjectId: Int): List<GitLabBuild> {
        return ArrayList<GitLabBuild>(gitLabBuilds[gitLabProjectId])
    }

    override fun registerGitLabProjectListener(listener: GitLabManager.GitLabProjectListener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterGitLabProjectListener(listener: GitLabManager.GitLabProjectListener) {
        listeners.remove(listener)
    }

    private suspend fun syncSync() {
        val gitLabProjectJson = gitLabApi.getGitLabProject()
        val gitLabProjects = gitLabProjectParser.parse(gitLabProjectJson)
        for (gitLabProject in gitLabProjects) {
            val gitLabBuildsDeferred = async(CommonPool) {
                val gitLabBuildsJson = gitLabApi.getGitLabBuild(gitLabProject.id)
                gitLabBuildParser.parse(gitLabBuildsJson, 4)
            }
            updateGitLabBuild(gitLabProject.id, gitLabBuildsDeferred.await())
        }
        updateGitLabProjects(gitLabProjects)
        notifyListener()
    }

    private fun updateGitLabProjects(gitLabProjects: List<GitLabProject>) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable {
                updateGitLabProjects(gitLabProjects)
            })
            return
        }
        this.gitLabProjects.clear()
        this.gitLabProjects.addAll(gitLabProjects)
    }

    private fun updateGitLabBuild(gitLabProjectId: Int, gitLabBuilds: List<GitLabBuild>) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable {
                updateGitLabBuild(gitLabProjectId, gitLabBuilds)
            })
            return
        }
        this.gitLabBuilds[gitLabProjectId] = gitLabBuilds
    }

    private fun notifyListener() {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable {
                notifyListener()
            })
            return
        }
        isSyncing = false
        for (listener in listeners) {
            listener.onGitLabProjectChanged()
        }
    }
}
