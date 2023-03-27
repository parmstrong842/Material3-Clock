package com.example.myclockapp.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Duration
import java.time.LocalTime


data class StopwatchUiState(
    val timer: String,
    val timerRunning: Boolean,
    val timerIsZero: Boolean
)

class StopwatchViewModel : ViewModel() {

    private val updateFrequency = 10L

    private val _uiState = MutableStateFlow(
        StopwatchUiState(
            "00:00.00",
            timerRunning = false,
            timerIsZero = true
        )
    )
    val uiState = _uiState.asStateFlow()

    private var startTime = LocalTime.now()
    var shouldBeRunning = false

    fun startTimer(resume: Boolean = false) {
        shouldBeRunning = true

        _uiState.update {
            it.copy(
                timerRunning = true,
                timerIsZero = false
            )
        }

        if (!resume) {
            startTime = LocalTime.now()
        }
        startLooping()
    }

    fun stopTimer() {
        shouldBeRunning = false

        _uiState.update {
            it.copy(
                timerRunning = false
            )
        }
    }

    private fun startLooping() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val deltaTime = getDeltaAsString(startTime, LocalTime.now())
                _uiState.update {
                    it.copy(
                        timer = deltaTime
                    )
                }
                if (shouldBeRunning) {
                    handler.postDelayed(this, updateFrequency)
                }
            }
        }
        handler.post(runnable)
    }

    private fun getDeltaAsString(start: LocalTime, now: LocalTime): String {
        val duration = Duration.between(start, now)
        val centi = duration.nano / 10_000_000
        val seconds = duration.seconds % 60
        val minutes = duration.seconds / 60
        return String.format("%02d:%02d.%02d", minutes, seconds, centi)
    }
}
