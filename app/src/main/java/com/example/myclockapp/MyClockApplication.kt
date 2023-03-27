package com.example.myclockapp

import android.app.Application
import com.example.myclockapp.data.BottomBarState

class MyClockApplication : Application() {
    lateinit var bottomBarState: BottomBarState
    override fun onCreate() {
        super.onCreate()
        bottomBarState = BottomBarState(this)
    }
}