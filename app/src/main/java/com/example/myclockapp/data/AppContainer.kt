package com.example.myclockapp.data

import android.content.Context

interface AppContainer {
    val alarmRepository: AlarmRepository
    val startScreenState: StartScreenState
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val alarmRepository: AlarmRepository by lazy {
        AlarmRepository()
    }
    override val startScreenState: StartScreenState by lazy {
        StartScreenState(context)
    }
}