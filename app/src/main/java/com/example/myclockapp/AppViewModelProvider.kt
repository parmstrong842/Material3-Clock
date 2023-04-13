package com.example.myclockapp

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myclockapp.ui.AlarmViewModel
import com.example.myclockapp.ui.MainContentViewModel
import com.example.myclockapp.ui.NewAlarmViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MainContentViewModel(myClockApplication().container.startScreenState)
        }
        initializer {
            AlarmViewModel(myClockApplication().container.alarmRepository)
        }
        initializer {
            NewAlarmViewModel(
                this.createSavedStateHandle(),
                myClockApplication().container.alarmRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [MyClockApplication].
 */
fun CreationExtras.myClockApplication(): MyClockApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyClockApplication)