package com.example.myclockapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.myclockapp.ui.AlarmActivity

object NotificationDispatcher {
    private const val channelId = "AlarmId"
    private const val alarmNotificationId = 100


    fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.alarm_notification_channel)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    fun fireAlarmNotification(context: Context, id: String, name: String, time: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(context.getString(R.string.extra_name), name)
            putExtra(context.getString(R.string.extra_time), time)
            putExtra(context.getString(R.string.alarm_id), id)
        }
        val alarmPendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT)

        val dismissIntent = Intent(context, MyReceiver::class.java).apply {
            action = context.getString(R.string.action_dismiss)
            putExtra(context.getString(R.string.alarm_id), id)
        }
        val dismissPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT)
        val dismissAction = NotificationCompat.Action(R.drawable.ic_launcher_foreground, "Dismiss", dismissPendingIntent)

        val snoozeIntent = Intent(context, MyReceiver::class.java).apply {
            action = context.getString(R.string.action_snooze)
            putExtra(context.getString(R.string.alarm_id), id)
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT)
        val snoozeAction = NotificationCompat.Action(R.drawable.ic_launcher_foreground, "Snooze", snoozePendingIntent)

        val notification: Notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Clock")
            .setContentText(name)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(alarmPendingIntent)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .addAction(dismissAction)
            .addAction(snoozeAction)
            .build()

        notificationManager.notify(alarmNotificationId, notification)
    }

    fun cancelAlarmNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(alarmNotificationId)
    }
}