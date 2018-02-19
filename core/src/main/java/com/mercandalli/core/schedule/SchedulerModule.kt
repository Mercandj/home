package com.mercandalli.core.schedule

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class SchedulerModule(
        private val application: Application) {

    fun provideScheduleManager(): ScheduleManager {
        return ScheduleManagerAlarmManager(
                application.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
                getAlarmManagerPendingIntent()
        )
    }

    private fun getAlarmManagerPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(application, 0,
                Intent(application, ScheduleReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
