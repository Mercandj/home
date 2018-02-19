package com.mercandalli.core.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScheduleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        if (context != null) {
            SchedulerService.startSchedule(context)
        }
    }
}