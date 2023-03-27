package com.example.myclockapp.ui

import androidx.lifecycle.ViewModel
import com.example.myclockapp.data.AlarmRepository
import com.example.myclockapp.model.Alarm

class AlarmViewModel() : ViewModel() {

    var updating: Boolean = false
    var currentAlarm: Alarm = Alarm()

    private val alarmRepository: AlarmRepository
        get() {
            return AlarmRepository()
        }

    fun fetchAlarms(): List<Alarm> {
        return alarmRepository.fetchAlarms()
    }

    fun addAlarm(alarm: Alarm) {
        alarmRepository.addAlarm(alarm)
    }
}