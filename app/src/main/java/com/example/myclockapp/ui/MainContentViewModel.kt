package com.example.myclockapp.ui

import androidx.lifecycle.ViewModel
import com.example.myclockapp.data.BottomBarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainContentUiState(
    val hideBottomBar: Boolean = false
)

class MainContentViewModel(private val bottomBarState: BottomBarState) : ViewModel() {
    private val _uiState = MutableStateFlow(MainContentUiState())
    val uiState = _uiState.asStateFlow()

    var startScreen = bottomBarState.getState()
        private set

    fun updateBottomBarState(newState: Int) {
        bottomBarState.setState(newState)
        startScreen = newState
    }

    fun hideBottomBar(b: Boolean) {
        _uiState.update {
            it.copy(
                hideBottomBar = b
            )
        }
    }
}