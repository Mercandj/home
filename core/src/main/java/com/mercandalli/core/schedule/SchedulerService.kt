package com.mercandalli.core.schedule

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.mercandalli.core.main.CoreGraph
import com.mercandalli.core.train.TrainManager
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE

class SchedulerService : IntentService("SchedulerService") {

    override fun onCreate() {
        super.onCreate()
        val notification = createPlayerNotification(this)
        startForeground(1, notification)
    }

    override fun onHandleIntent(intent: Intent?) {
        val coreGraph = CoreGraph.get()
        val provideTrainManager = coreGraph.provideTrainManager()
        val trainTrafficD = provideTrainManager.trainTrafficSync(TrainManager.TRAFFIC_D)
        val trainTrafficA = provideTrainManager.trainTrafficSync(TrainManager.TRAFFIC_A)
        val trafficSchedulesYerresD = provideTrainManager.trainTrafficSchedules(TrainManager.SCHEDULES_YERRES_D)
        val trafficSchedulesGdlD = provideTrainManager.trainTrafficSchedules(TrainManager.SCHEDULES_GARE_DE_LYON_D)

        val notificationMessage = StringBuilder(hoursMinutes()).append("\n\n")
        if (trainTrafficD != null) {
            notificationMessage.append("TrainTrafficD: ").append(trainTrafficD.message).append("\n")
        }
        if (trainTrafficA != null) {
            notificationMessage.append("TrainTrafficA: ").append(trainTrafficA.message).append("\n")
        }
        notificationMessage.append("\n")
        if (trafficSchedulesYerresD != null && trafficSchedulesYerresD.schedules.size > 1) {
            notificationMessage.append("TrainSchedulesYerres: \n")
                    .append(trafficSchedulesYerresD.schedules[0].message).append("\n")
                    .append(trafficSchedulesYerresD.schedules[1].message).append("\n")
        }
        notificationMessage.append("\n")
        if (trafficSchedulesGdlD != null && trafficSchedulesGdlD.schedules.size > 1) {
            notificationMessage.append("TrainSchedulesGdlD: \n")
                    .append(trafficSchedulesGdlD.schedules[0].message).append("\n")
                    .append(trafficSchedulesGdlD.schedules[1].message).append("\n")
        }
        coreGraph.provideNotificationManager().showNotification(notificationMessage.toString())
    }

    private fun hoursMinutes(): String {
        val calendar = Calendar.getInstance()
        val hours = calendar.get(HOUR_OF_DAY)
        val minutes = calendar.get(MINUTE)
        return toDecimalString(hours) + ":" + toDecimalString(minutes)
    }

    private fun toDecimalString(nb: Int): String {
        if (nb > 9) {
            return nb.toString()
        }
        return "0" + nb
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
