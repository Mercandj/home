package com.mercandalli.core.schedule

interface ScheduleManager {

    fun schedule(timestamp: Long)

    fun scheduleRepeating(timestamp: Long, repeatTime: Long)

}