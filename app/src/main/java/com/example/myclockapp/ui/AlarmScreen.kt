package com.example.myclockapp.ui


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myclockapp.model.Alarm

@Composable
fun AlarmScreen(
    onNewAlarmClick: () -> Unit,
    editAlarmClick: (Alarm) -> Unit,
    alarms: List<Alarm>,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues
) {
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
        alarms.forEach {
            list.add(AlarmItem(it.time.substring(0..it.time.length-3), alarm = it))
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .clip(RoundedCornerShape(16.dp))
        ) {
            items(list) {
                AlarmCard(alarmItem = it, editAlarmClick)
            }
        }
    }
}

@Composable
private fun AlarmCard(
    alarmItem: AlarmItem,
    editAlarmClick: (Alarm) -> Unit
) {
    var checked by remember { mutableStateOf(false)}

    val color = if (checked) Color.Black else Color.Gray

    Card(
        modifier = Modifier
            .clickable {
                editAlarmClick(alarmItem.alarm)
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
                    modifier = Modifier.padding(start = 16.dp).alignByBaseline(),
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
                    onCheckedChange = { checked = !checked },
                    modifier = Modifier
                        .padding(end = 16.dp)
                )
            }
        }
    }
}


data class AlarmItem(
    val time: String = "6:00",
    val period: String = "AM",
    val date: String = "Tue, Nov 15",
    val alarm: Alarm
)

@Preview(widthDp = 393, heightDp = 808)
@Composable
fun AlarmScreenPreview() {
    AlarmScreen(
        {},
        {},
        emptyList(),
        innerPadding = PaddingValues(0.dp)
    )
}