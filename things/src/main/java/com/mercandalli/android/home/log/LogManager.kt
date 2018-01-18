package com.mercandalli.android.home.log

interface LogManager {

    val logs: String

    fun log(tag: String, log: String)

    fun log(log: String)

    fun log(log: String, e: Exception)

    fun log(tag: String, log: String, e: Exception)

}
