package com.mercandalli.core.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ScheduleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        Log.d("jm/debug", "Test: " + context)
        if (context != null) {
            SchedulerService.startSchedule(context)
        }
    }
}