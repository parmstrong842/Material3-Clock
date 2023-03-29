package com.example.myclockapp.ui

import androidx.lifecycle.ViewModel
import com.example.myclockapp.data.StartScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainContentUiState(
    val bottomBarState: Boolean = true
)

class MainContentViewModel(private val startScreenState: StartScreenState) : ViewModel() {
    private val _uiState = MutableStateFlow(MainContentUiState())
    val uiState = _uiState.asStateFlow()

    var startScreen = startScreenState.getState()
        private set

    fun updateStartScreenState(newState: Int) {
        startScreenState.setState(newState)
        startScreen = newState
    }

    fun bottomBarState(b: Boolean) {
        _uiState.update {
            it.copy(
                bottomBarState = b
            )
        }
    }
}