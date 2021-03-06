package com.mercandalli.core.gitlab

import com.mercandalli.core.main_thread.MainThreadPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/* package */ class GitLabManagerImpl(
    private val gitLabApi: GitLabApi,
    private val gitLabProjectParser: GitLabProjectParser,
    private val gitLabBuildParser: GitLabBuildParser,
    private val mainThreadPost: MainThreadPost
) : GitLabManager {

    private val gitLabProjects = ArrayList<GitLabProject>()
    private val gitLabBuilds = HashMap<Int, List<GitLabBuild>>()
    private val listeners = ArrayList<GitLabManager.GitLabProjectListener>()
    private var isSyncing = false

    override fun sync(): Boolean {
        if (isSyncing) {
            return false
        }
        isSyncing = true
        GlobalScope.async(Dispatchers.Default) { syncSync() }
        return true
    }

    override fun getGitLabProjects(): List<GitLabProject> {
        val list = ArrayList<GitLabProject>()
        @Suppress("LoopToCallChain")
        for (gitLabProject in gitLabProjects) {
            if (gitLabBuilds.containsKey(gitLabProject.id)) {
                list.add(gitLabProject)
            }
        }
        return list
    }

    override fun getGitLabBuild(gitLabProjectId: Int): List<GitLabBuild> {
        if (!gitLabBuilds.containsKey(gitLabProjectId)) {
            return ArrayList()
        }
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

    private fun syncSync() {
        val gitLabProjectJson = gitLabApi.getGitLabProject()
        val gitLabProjects = gitLabProjectParser.parse(gitLabProjectJson).toMutableList()
        updateGitLabProjects(gitLabProjects)
        val listIterator = gitLabProjects.listIterator()
        while (listIterator.hasNext()) {
            val gitLabProject = listIterator.next()
            val gitLabBuildsJson = gitLabApi.getGitLabBuild(gitLabProject.id)
            val gitLabBuilds = gitLabBuildParser.parse(gitLabBuildsJson, 4)
            if (gitLabBuilds.isEmpty()) {
                listIterator.remove()
            } else {
                updateGitLabBuild(gitLabProject.id, gitLabBuilds)
                notifyListener()
            }
        }
        updateGitLabProjects(gitLabProjects)
        notifyListener()
        isSyncing = false
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
        for (listener in listeners) {
            listener.onGitLabProjectChanged()
        }
    }
}
