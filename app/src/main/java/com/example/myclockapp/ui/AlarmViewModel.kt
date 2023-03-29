package com.example.myclockapp.ui

import androidx.lifecycle.ViewModel
import com.example.myclockapp.data.AlarmRepository
import com.example.myclockapp.model.Alarm

class AlarmViewModel(private val alarmRepository: AlarmRepository) : ViewModel() {

    fun fetchAlarms(): List<Alarm> {
        return alarmRepository.fetchAlarms()
    }

    fun addAlarm(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
    }
}