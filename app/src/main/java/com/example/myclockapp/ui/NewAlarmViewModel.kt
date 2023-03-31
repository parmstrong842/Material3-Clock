package com.example.myclockapp.ui

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclockapp.AlarmScheduler
import com.example.myclockapp.data.AlarmRepository
import com.example.myclockapp.model.Alarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AlarmUiState(
    val time: String = "6:00AM",
    val name: String = "---",
    val sunSelected: Boolean = false,
    val monSelected: Boolean = false,
    val tueSelected: Boolean = false,
    val wedSelected: Boolean = false,
    val thuSelected: Boolean = false,
    val friSelected: Boolean = false,
    val satSelected: Boolean = false,
)

class NewAlarmViewModel(
    savedStateHandle: SavedStateHandle,
    private val alarmRepository: AlarmRepository
) : ViewModel() {
    private val alarmId: Int = checkNotNull(savedStateHandle["alarmId"])
    var isNewAlarm: Boolean = true

    private val _uiState: MutableStateFlow<AlarmUiState>
    val uiState: StateFlow<AlarmUiState>

    init {
        viewModelScope.launch {
            val alarm = alarmRepository.getItem(alarmId)
            if (alarm != null) updateAlarmUiState(alarm)
            isNewAlarm = alarm == null
        }

        _uiState = MutableStateFlow(AlarmUiState())
        uiState = _uiState.asStateFlow()
    }

    var mHour = 0
        private set
    var mMinute = 0
        private set

    private fun updateAlarmUiState(alarm: Alarm) {
        _uiState.update {
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
        }
    }


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

    fun updateHourAndMinute(hour: Int, minute: Int) {
        this.mHour = hour
        this.mMinute = minute
    }

    fun insertAlarm(context: Context) {
        val alarm = Alarm(
            time = _uiState.value.time,
            hour = mHour,
            minute = mMinute,
            enabled = true,
            sun = _uiState.value.sunSelected,
            mon = _uiState.value.monSelected,
            tue = _uiState.value.tueSelected,
            wed = _uiState.value.wedSelected,
            thu = _uiState.value.thuSelected,
            fri = _uiState.value.friSelected,
            sat = _uiState.value.satSelected,
            name = _uiState.value.name
        )

        //TODO figure out id, probably generate it myself
        AlarmScheduler.schedule(context, alarm)

        viewModelScope.launch {
            alarmRepository.insertItem(alarm)
        }
    }

    fun updateAlarm(context: Context) {
        val alarm = Alarm(
            alarmId,
            true,
            _uiState.value.time,
            mHour,
            mMinute,
            _uiState.value.sunSelected,
            _uiState.value.monSelected,
            _uiState.value.tueSelected,
            _uiState.value.wedSelected,
            _uiState.value.thuSelected,
            _uiState.value.friSelected,
            _uiState.value.satSelected,
            _uiState.value.name
        )

        AlarmScheduler.schedule(context, alarm)

        viewModelScope.launch {
            alarmRepository.updateItem(alarm)
        }
    }
}