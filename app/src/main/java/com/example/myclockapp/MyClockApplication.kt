package com.example.myclockapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.myclockapp.data.AppContainer
import com.example.myclockapp.data.AppDataContainer
import com.example.myclockapp.ui.NotificationDispatcher

class MyClockApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        NotificationDispatcher.createNotificationChannel(this)
    }
}