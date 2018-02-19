package com.mercandalli.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import com.mercandalli.core.R

internal class NotificationManagerImpl(
        context: Context,
        private val notificationManager: android.app.NotificationManager) : NotificationManager {

    private val context: Context = context.applicationContext

    override fun showNotification(message: String) {
        // The id of the channel.
        val id = "profiler_notification_channel"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // The user-visible name of the channel.
            val name = "Profiler channel"
            // The user-visible description of the channel.
            val description = "Message from the profiler app"
            val importance = android.app.NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance)
            // Configure the notification channel.
            channel.description = description
            channel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(channel)
        }

        sendNotification(
                context,
                Intent(),
                message,
                context.getString(R.string.app_name),
                R.drawable.ic_whatshot_red_24dp, null,
                Color.RED,
                42,
                id
        )
    }

    override fun getNotification(message: String): Notification {
        // The id of the channel.
        val id = "profiler_notification_channel"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // The user-visible name of the channel.
            val name = "Profiler channel"
            // The user-visible description of the channel.
            val description = "Message from the profiler app"
            val importance = android.app.NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance)
            // Configure the notification channel.
            channel.description = description
            channel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(channel)
        }

        return getNotification(
                context,
                Intent(),
                message,
                "app_name",
                R.drawable.ic_whatshot_red_24dp,
                null,
                Color.RED,
                id
        )
    }

    private fun sendNotification(
            context: Context,
            intent: Intent,
            message: String?,
            title: String?,
            @DrawableRes smallIcon: Int,
            largeIcon: Bitmap?,
            @ColorInt light: Int,
            notificationId: Int,
            channelId: String) {
        notificationManager.notify(notificationId, getNotification(
                context,
                intent,
                message,
                title,
                smallIcon,
                largeIcon,
                light,
                channelId))
    }

    private fun getNotification(
            context: Context,
            intent: Intent,
            message: String?,
            title: String?,
            @DrawableRes smallIcon: Int,
            largeIcon: Bitmap?,
            @ColorInt light: Int,
            channelId: String): Notification {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        val contentIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcon)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setLights(light, 500, 1000)
                .setContentText(message)
                .setContentTitle(title)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setOngoing(false)
        if (largeIcon != null) {
            builder.setLargeIcon(largeIcon)
        }
        builder.setContentIntent(contentIntent)
        return builder.build()
    }
}
