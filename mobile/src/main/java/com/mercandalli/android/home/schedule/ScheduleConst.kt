package com.mercandalli.android.home.schedule

import com.mercandalli.core.main.CoreGraph
import java.util.*
import java.util.concurrent.TimeUnit

class ScheduleConst {

    companion object {

        fun scheduleTrafficNotification() {
            val provideScheduleManager = CoreGraph.get().provideScheduleManager()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 6)
            calendar.set(Calendar.MINUTE, 46)
            provideScheduleManager.scheduleRepeating(
                    calendar.timeInMillis,
                    TimeUnit.DAYS.toMillis(1))
            calendar.set(Calendar.HOUR_OF_DAY, 19)
            calendar.set(Calendar.MINUTE, 27)
            provideScheduleManager.scheduleRepeating(
                    calendar.timeInMillis,
                    TimeUnit.DAYS.toMillis(1))
        }

    }

}