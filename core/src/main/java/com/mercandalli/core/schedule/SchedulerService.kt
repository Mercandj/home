package com.mercandalli.core.schedule

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat

class SchedulerService : IntentService("SchedulerService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null && ACTION_SCHEDULE == intent.action) {
            val notification = createPlayerNotification(this)
            startForeground(1, notification)
        }
    }

    companion object {

        private const val ACTION_SCHEDULE = "SchedulerService.Actions.ACTION_SCHEDULE"

        /* package */
        internal fun startSchedule(context: Context) {
            val intent = Intent(context, SchedulerService::class.java)
            intent.action = ACTION_SCHEDULE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        /**
         * Create the player [Notification].
         *
         * @param context a [Context] used for creating the [PendingIntent]s.
         * @return the newly created [Notification].
         */
        @SuppressLint("NewApi")
        private fun createPlayerNotification(context: Context): Notification {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "tube_notification_channel"
            val name = "Tube channel"
            val description = "Message from the Tube app"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            notificationManager.createNotificationChannel(channel)

            val builder = NotificationCompat.Builder(context, channelId)
                    .setContentTitle("string.app_name")
                    .setContentText("Scheduler")

            return builder.build()
        }
    }
}
