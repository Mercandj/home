package com.mercandalli.android.home.log

import android.util.Log
import java.util.*

class LogManagerImpl private constructor() : LogManager {

    private val logsArray = ArrayList<String>()

    override val logs: String
        get() = getLogsInternal()

    override val systemLogs: String
        get() = getSystemLogsInternal()

    override fun log(tag: String, log: String) {
        var log = log
        log = "[$tag] $log"
        logsArray.add(log)
        Log.d("jm/debug", log)
    }

    override fun log(log: String) {
        logsArray.add(log)
        Log.d("jm/debug", log)
    }

    override fun log(log: String, e: Exception) {
        logsArray.add(log)
        Log.e("jm/debug", log, e)
    }

    override fun log(tag: String, log: String, e: Exception) {
        var log = log
        log = "[$tag] $log"
        logsArray.add(log)
        Log.d("jm/debug", log, e)
    }

    private fun getLogsInternal(): String {
        val stringBuilder = StringBuilder()
        for (log in logs) {
            stringBuilder.append(log).append("\n")
        }
        return stringBuilder.toString()
    }

    private fun getSystemLogsInternal(): String {
        return LogsUtils.logs()
    }

    companion object {

        @JvmStatic
        val instance: LogManager = LogManagerImpl()
    }
}
