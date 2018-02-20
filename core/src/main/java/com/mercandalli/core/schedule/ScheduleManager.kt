package com.mercandalli.core.schedule

interface ScheduleManager {

    fun initialize()

    fun schedule(
            timestamp: Long,
            repeatInterval: Long = -1)

}