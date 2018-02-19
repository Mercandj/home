package com.mercandalli.core.schedule

import android.app.AlarmManager
import android.app.PendingIntent

class ScheduleManagerAlarmManager(
        private val alarmManager: AlarmManager,
        private val pendingIntent: PendingIntent) : ScheduleManager {
    override fun schedule(timestamp: Long) {
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                timestamp,
                pendingIntent)
    }

    override fun scheduleRepeating(timestamp: Long, repeatTime: Long) {
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                timestamp,
                repeatTime,
                pendingIntent)

    }
}