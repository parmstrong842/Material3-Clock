package com.example.myclockapp.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.myclockapp.R

object NotificationDispatcher {
    private const val channelId = "AlarmId"
    private const val alarmNotificationId = 100


    fun createNotificationChannel(context: Context) {
        val name = "AlarmNotification"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    fun fireAlarmNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, AlarmActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification: Notification = Notification.Builder(context, channelId)
            .setContentTitle("title")
            .setContentText("msg")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .build()

        notificationManager.notify(alarmNotificationId, notification)
    }
}