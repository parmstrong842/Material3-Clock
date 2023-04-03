package com.example.myclockapp.ui


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myclockapp.AlarmScheduler
import com.example.myclockapp.AppViewModelProvider
import com.example.myclockapp.MyReceiver
import com.example.myclockapp.model.Alarm
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun AlarmScreen(
    onNewAlarmClick: () -> Unit,
    editAlarmClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    viewModel: AlarmViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState by viewModel.uiState.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row {
            Text(
                text = "Alarm",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp)
            )
            IconButton(onClick = onNewAlarmClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.clip(RoundedCornerShape(24.dp))
                ) {
                    /*DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
                        Text("Set bedtime and wake-up time")
                    }
                    DropdownMenuItem(onClick = { /* Handle settings! */ }) {
                        Text("Edit")
                    }
                    DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
                        Text("Send Sort")
                    }
                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text("Settings")
                    }*/
                }
            }
        }
        val list: MutableList<AlarmItem> = mutableListOf()
        uiState.alarms.forEach {
            list.add(AlarmItem(it.time.substring(0..it.time.length-3), alarm = it))
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .clip(RoundedCornerShape(16.dp))
        ) {
            items(list) {
                AlarmCard(
                    alarmItem = it,
                    editAlarmClick = editAlarmClick,
                    updateAlarm = { alarm -> viewModel.updateAlarm(alarm) }
                )
            }
        }
    }
}

@Composable
private fun AlarmCard(
    alarmItem: AlarmItem,
    editAlarmClick: (String) -> Unit,
    updateAlarm: (Alarm) -> Unit
) {
    val checked = alarmItem.alarm.enabled
    val context = LocalContext.current
    val color = if (checked) Color.Black else Color.Gray

    Card(
        modifier = Modifier
            .clickable {
                editAlarmClick(alarmItem.alarm.id)
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(100.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = alarmItem.time,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .alignByBaseline(),
                    color = color
                )
                Text(
                    text = alarmItem.period,
                    style = MaterialTheme.typography.titleMedium,
                    color = color,
                    modifier = Modifier.alignByBaseline()
                )
                Box(Modifier.weight(1f))
                Text(
                    text = alarmItem.date,
                    style = MaterialTheme.typography.bodyLarge,
                    color = color,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        onCheckChanged(context, it, alarmItem.alarm, updateAlarm)
                    },
                    modifier = Modifier
                        .padding(end = 16.dp)
                )
            }
        }
    }
}

private fun onCheckChanged(
    context: Context,
    checked: Boolean,
    alarm: Alarm,
    updateAlarm: (Alarm) -> Unit
) {
    if (checked) {
        updateAlarm(alarm.copy(enabled = true))
        AlarmScheduler.schedule(context, alarm)
    } else {
        updateAlarm(alarm.copy(enabled = false))
        AlarmScheduler.unschedule(context, alarm)
    }
}

data class AlarmItem(
    val time: String = "6:00",
    val period: String = "AM",
    val date: String = "date placeholder",
    val alarm: Alarm
)