package com.example.myclockapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun StopwatchScreen(
    innerPadding: PaddingValues,
    viewModel: StopwatchViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()


    Column(Modifier.padding(innerPadding)) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Settings")
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            Text(
                text = uiState.timer,
                fontSize = 40.sp,
                modifier = Modifier.offset { IntOffset(0, -100.dp.roundToPx()) }
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {}){
                Text("Reset")
            }
            if (!uiState.timerRunning && uiState.timerIsZero) {
                Button(onClick = { viewModel.startTimer() }) {
                    Text("Start")
                }
            } else if (!uiState.timerRunning) {
                Button(onClick = { viewModel.startTimer(resume = true) }) {
                    Text("Resume")
                }
            } else {
                Button(onClick = { viewModel.stopTimer() }) {
                    Text("Stop")
                }
            }
        }
    }
}