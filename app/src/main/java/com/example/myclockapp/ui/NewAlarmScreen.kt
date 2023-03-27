package com.example.myclockapp.ui

import android.widget.TimePicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myclockapp.data.AlarmDummyData
import com.example.myclockapp.model.Alarm
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun NewAlarmScreen(
    innerPadding: PaddingValues,
    onCancelClicked: () -> Unit,
    onSaveClicked: (Alarm) -> Unit,
    handleBackButton: () -> Unit,
    currentAlarm: Alarm,
) {
    //BackHandler(onBack = handleBackButton)

    Column(Modifier.padding(innerPadding)) {

        var sunSelected by remember { mutableStateOf(currentAlarm.sun) }
        var monSelected by remember { mutableStateOf(currentAlarm.mon) }
        var tueSelected by remember { mutableStateOf(currentAlarm.tue) }
        var wedSelected by remember { mutableStateOf(currentAlarm.wed) }
        var thuSelected by remember { mutableStateOf(currentAlarm.thu) }
        var friSelected by remember { mutableStateOf(currentAlarm.fri) }
        var satSelected by remember { mutableStateOf(currentAlarm.sat) }

        var time by remember { mutableStateOf("")}

        // Adds view to Compose
        AndroidView(
            modifier = Modifier.fillMaxWidth(), // Occupy the max size in the Compose UI tree
            factory = { context ->
                // Creates view
                TimePicker(context)
            },
            update = { view ->
                val temp = get24Time(currentAlarm.time)
                view.hour = temp.substring(0, 2).toInt()
                view.minute = temp.substring(3).toInt()
                time = "${view.hour}:${view.minute}"
                view.setOnTimeChangedListener { _, mHour, mMinute ->
                    time = "$mHour:$mMinute"
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .weight(1f)
        ) {

            var text = ""
            if(sunSelected && monSelected && tueSelected && wedSelected && thuSelected && friSelected && satSelected) {
                text += "Every day"
            } else if (!sunSelected && !monSelected && !tueSelected && !wedSelected && !thuSelected && !friSelected && !satSelected) {
                val current = LocalDateTime.now().plusDays(1)
                val formatter = DateTimeFormatter.ofPattern("E, MMM d")
                val formatted = current.format(formatter)
                text += "Tomorrow-$formatted"
            } else {
                text += "Every "
                if(sunSelected) { text += "Sun" }
                if(monSelected) { text += ", Mon" }
                if(tueSelected) { text += ", Tue" }
                if(wedSelected) { text += ", Wed" }
                if(thuSelected) { text += ", Thu" }
                if(friSelected) { text += ", Fri" }
                if(satSelected) { text += ", Sat" }
            }
            val newText = text.replace(" ,", "")

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = newText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null)
                }
            }

            Row (horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()){
                DayItem(text = "S", selected = sunSelected) { sunSelected = !sunSelected }
                DayItem(text = "M", selected = monSelected) { monSelected = !monSelected }
                DayItem(text = "T", selected = tueSelected) { tueSelected = !tueSelected }
                DayItem(text = "W", selected = wedSelected) { wedSelected = !wedSelected }
                DayItem(text = "T", selected = thuSelected) { thuSelected = !thuSelected }
                DayItem(text = "F", selected = friSelected) { friSelected = !friSelected }
                DayItem(text = "S", selected = satSelected) { satSelected = !satSelected }
            }
            Spacer(Modifier.padding(16.dp))
            var textFieldValue by remember { mutableStateOf("")}
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                placeholder = { Text("Alarm name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.padding(16.dp))
            SettingItem("Alarm sound", "Moon Discovery")
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
            SettingItem(setting = "Vibration", selection = "Basic call")
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
            SettingItem(setting = "Snooze", selection = "5 minutes, 3 times")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(onClick = onCancelClicked, Modifier.width(128.dp)) {
                Text("Cancel")
            }
            TextButton(
                onClick = {
                    onSaveClicked(newAlarm(time, sunSelected, monSelected, tueSelected, wedSelected, thuSelected, friSelected, satSelected))
                },
                Modifier.width(128.dp)
            ) {
                Text("Save")
            }
        }
    }
}

private fun newAlarm(time: String, sunSelected: Boolean, monSelected: Boolean, tueSelected: Boolean, wedSelected: Boolean, thuSelected: Boolean, friSelected: Boolean, satSelected: Boolean): Alarm {
    val sDF24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sDF12 = SimpleDateFormat("hh:mma", Locale.getDefault())
    val dT24: Date = sDF24.parse(time) as Date
    var newTime = sDF12.format(dT24)
    if(newTime[0] == '0')
        newTime = newTime.substring(1)

    return Alarm(AlarmDummyData.id++, newTime, sunSelected, monSelected, tueSelected, wedSelected, thuSelected, friSelected, satSelected)
}

private fun get24Time(time: String): String {
    val temp = if(time.length == 6) "0$time" else time

    val sDF24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sDF12 = SimpleDateFormat("hh:mma", Locale.getDefault())
    val dT12: Date = sDF12.parse(temp) as Date
    return sDF24.format(dT12)
}


@Composable
private fun DayItem(
    text: String,
    selected: Boolean,
    toggleSelection: () -> Unit
) {
    val modifier = Modifier.width(24.dp)

    TextButton(
        onClick = toggleSelection,
        shape = CircleShape,
        modifier = if (selected) modifier.border(1.dp, Color.Blue, CircleShape) else modifier
    ) {
        Text(text)
    }
}

@Composable
private fun SettingItem(
    setting: String,
    selection: String,
) {
    var checked by remember { mutableStateOf(false)}

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(16.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(setting)
            Text(selection)
        }
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .width(1.dp)
        )
        Switch(
            checked = checked,
            onCheckedChange = { checked = !checked},
            modifier = Modifier
                .padding(end = 16.dp)
        )
    }
}

/*
@Preview(widthDp = 393)
@Composable
fun NewAlarmPreview() {
    NewAlarmScreen(innerPadding = PaddingValues(0.dp), {}, {}, Alarm(), false)
}
*/

