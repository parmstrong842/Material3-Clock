package com.example.myclockapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclockapp.data.AlarmRepository
import com.example.myclockapp.model.Alarm
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AlarmScreenUiState(
    val alarms: List<Alarm> = listOf()
)

class AlarmViewModel(private val alarmRepository: AlarmRepository) : ViewModel() {

    val uiState: StateFlow<AlarmScreenUiState> =
        alarmRepository.getAllItems().map { AlarmScreenUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AlarmScreenUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmRepository.updateItem(alarm)
        }
    }
}