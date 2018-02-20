package com.mercandalli.core.schedule

data class Alarm(
        val timestamp: Long,
        val repeatInterval: Long = -1)