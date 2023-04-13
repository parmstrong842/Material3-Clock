package com.example.myclockapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myclockapp.NotificationDispatcher
import com.example.myclockapp.R
import com.example.myclockapp.ui.theme.MyClockAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationDispatcher.cancelAlarmNotification(this)

        val id = intent.getStringExtra(getString(R.string.alarm_id)) ?: "bad id"
        val time = intent.getStringExtra(getString(R.string.extra_time)) ?: "bad time"
        val name = intent.getStringExtra(getString(R.string.extra_name)) ?: "bad name"

        setContent {
            MyClockAppTheme {

                val snoozeTime = remember { mutableStateOf(5) }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = time
                        )
                        Text(
                            text = name
                        )
                        Button(onClick = { finish() }) {
                            Text(
                                text = "Dismiss"
                            )
                        }
                        Row {
                            IconButton(onClick = {
                                if (snoozeTime.value > 5) {
                                    snoozeTime.value -= 5
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "remove")
                            }
                            Button(onClick = {

                            }) {//TODO get current time and make new alarm with time + 5 and id
                                Text(
                                    text = "Snooze ${snoozeTime.value}"
                                )
                            }
                            IconButton(onClick = {
                                if (snoozeTime.value < 60) {
                                    snoozeTime.value += 5
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                            }
                        }
                    }
                }
            }
        }
    }
}