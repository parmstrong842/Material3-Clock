package com.example.myclockapp.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.myclockapp.data.AlarmDummyData
import com.example.myclockapp.data.AlarmRepository
import com.example.myclockapp.model.Alarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class AlarmUiState(
    val time: String,
    val name: String,
    val sunSelected: Boolean,
    val monSelected: Boolean,
    val tueSelected: Boolean,
    val wedSelected: Boolean,
    val thuSelected: Boolean,
    val friSelected: Boolean,
    val satSelected: Boolean,
)

class NewAlarmViewModel(
    savedStateHandle: SavedStateHandle,
    private val alarmRepository: AlarmRepository
) : ViewModel() {
    private val alarmId: Int = checkNotNull(savedStateHandle["alarmId"])
    private val alarm = alarmRepository.fetchAlarm(alarmId)
    val isNewAlarm = alarm.id == -1

    private val _uiState = MutableStateFlow(
        AlarmUiState(
            alarm.time,
            alarm.name,
            alarm.sun,
            alarm.mon,
            alarm.tue,
            alarm.wed,
            alarm.thu,
            alarm.fri,
            alarm.sat,
        )
    )
    val uiState = _uiState.asStateFlow()

    fun updateDaySelected(i: Int) {
        when(i) {
            0 -> _uiState.update { it.copy(sunSelected = !_uiState.value.sunSelected) }
            1 -> _uiState.update { it.copy(monSelected = !_uiState.value.monSelected) }
            2 -> _uiState.update { it.copy(tueSelected = !_uiState.value.tueSelected) }
            3 -> _uiState.update { it.copy(wedSelected = !_uiState.value.wedSelected) }
            4 -> _uiState.update { it.copy(thuSelected = !_uiState.value.thuSelected) }
            5 -> _uiState.update { it.copy(friSelected = !_uiState.value.friSelected) }
            6 -> _uiState.update { it.copy(satSelected = !_uiState.value.satSelected) }
        }
    }

    fun updateTime(newTime: String) {
        _uiState.update {
            it.copy(
                time = newTime
            )
        }
    }

    fun updateName(newName: String) {
        _uiState.update {
            it.copy(
                name = newName
            )
        }
    }

    fun insertAlarm() {
        alarmRepository.insertAlarm(makeNewAlarm())
    }

    fun updateAlarm() {
        alarmRepository.updateAlarm(alarmId, makeNewAlarm())
    }

    private fun makeNewAlarm(): Alarm {
        return Alarm(
            AlarmDummyData.id,
            _uiState.value.time,
            _uiState.value.sunSelected,
            _uiState.value.monSelected,
            _uiState.value.tueSelected,
            _uiState.value.wedSelected,
            _uiState.value.thuSelected,
            _uiState.value.friSelected,
            _uiState.value.satSelected,
            _uiState.value.name
        )
    }
}