package com.mercandalli.core.notification

import android.app.Application
import android.content.Context

class NotificationModule {

    fun provideNotificationManager(application: Application): NotificationManager {
        val notificationSystemService = application.getSystemService(Context.NOTIFICATION_SERVICE)
        val notificationManager = notificationSystemService as android.app.NotificationManager
        return NotificationManagerImpl(application, notificationManager)
    }
}
