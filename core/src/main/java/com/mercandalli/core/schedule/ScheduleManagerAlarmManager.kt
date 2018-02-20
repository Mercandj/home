package com.mercandalli.core.schedule

import android.app.AlarmManager
import android.app.PendingIntent

class ScheduleManagerAlarmManager(
        private val alarmManager: AlarmManager,
        private val pendingIntent: PendingIntent) : ScheduleManager {

    private val alarms = ArrayList<Alarm>()

    override fun initialize() {

    }

    override fun schedule(
            timestamp: Long,
            repeatInterval: Long) {
        schedule(Alarm(timestamp, repeatInterval))
    }

    fun schedule(alarm: Alarm) {
        if (alarm.repeatInterval <= 0) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarm.timestamp,
                    pendingIntent)
        } else {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    alarm.timestamp,
                    alarm.repeatInterval,
                    pendingIntent)
        }
        alarms.add(alarm)
    }
}