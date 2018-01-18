package com.mercandalli.android.home.log

import android.util.Log
import java.util.*

class LogManagerImpl private constructor() : LogManager {

    private val logsArray = ArrayList<String>()

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


    override val logs: String
        get() = getLogsInternal()

    private fun getLogsInternal(): String {
        val stringBuilder = StringBuilder()
        for (log in logs) {
            stringBuilder.append(log).append("\n")
        }
        return stringBuilder.toString()
    }

    companion object {

        @JvmStatic
        val instance: LogManager = LogManagerImpl()
    }
}
