package com.example.myclockapp

import android.app.Application
import com.example.myclockapp.data.AppContainer
import com.example.myclockapp.data.AppDataContainer

class MyClockApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        NotificationDispatcher.createNotificationChannel(this)
    }
}