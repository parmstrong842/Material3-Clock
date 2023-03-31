package com.example.myclockapp.ui

import android.widget.TimePicker
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myclockapp.AppViewModelProvider
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun NewAlarmScreen(
    onCancelClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    viewModel: NewAlarmViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    Column {
        val sunSelected = uiState.sunSelected
        val monSelected = uiState.monSelected
        val tueSelected = uiState.tueSelected
        val wedSelected = uiState.wedSelected
        val thuSelected = uiState.thuSelected
        val friSelected = uiState.friSelected
        val satSelected = uiState.satSelected

        val time = uiState.time

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                TimePicker(context)
            },
            update = { view ->
                val temp = get24Time(uiState.time)
                val hour = temp.substring(0, 2).toInt()
                val minute = temp.substring(3).toInt()
                view.hour = hour
                view.minute = minute
                viewModel.updateHourAndMinute(hour, minute)
                view.setOnTimeChangedListener { _, mHour, mMinute ->
                    viewModel.updateTime(get12Time("$mHour:$mMinute"))
                    viewModel.updateHourAndMinute(mHour, mMinute)
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
                val current = LocalDateTime.now()
                var day = "Today"

                val now = LocalTime.now()
                val clock = LocalTime.of(viewModel.mHour, viewModel.mMinute)
                if (now.isAfter(clock)) {
                    current.plusDays(1)
                    day = "Tomorrow"
                }

                val formatter = DateTimeFormatter.ofPattern("E, MMM d")
                val formatted = current.format(formatter)
                text += "$day-$formatted"
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
                DayItem(text = "S", selected = sunSelected) { viewModel.updateDaySelected(0) }
                DayItem(text = "M", selected = monSelected) { viewModel.updateDaySelected(1) }
                DayItem(text = "T", selected = tueSelected) { viewModel.updateDaySelected(2) }
                DayItem(text = "W", selected = wedSelected) { viewModel.updateDaySelected(3) }
                DayItem(text = "T", selected = thuSelected) { viewModel.updateDaySelected(4) }
                DayItem(text = "F", selected = friSelected) { viewModel.updateDaySelected(5) }
                DayItem(text = "S", selected = satSelected) { viewModel.updateDaySelected(6) }
            }
            Spacer(Modifier.padding(16.dp))
            TextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
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
                    if (viewModel.isNewAlarm) {
                        viewModel.insertAlarm(context)
                    } else {
                        viewModel.updateAlarm(context)
                    }
                    onSaveClicked()
                },
                Modifier.width(128.dp)
            ) {
                Text("Save")
            }
        }
    }
}

private fun get24Time(time: String): String {
    val temp = if(time.length == 6) "0$time" else time

    val sDF24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sDF12 = SimpleDateFormat("hh:mma", Locale.getDefault())
    val dT12: Date = sDF12.parse(temp) as Date
    return sDF24.format(dT12)
}

private fun get12Time(time: String): String {
    val sDF24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sDF12 = SimpleDateFormat("h:mma", Locale.getDefault())
    val dT24: Date = sDF24.parse(time) as Date
    return sDF12.format(dT24)
}

@Composable
private fun DayItem(
    text: String,
    selected: Boolean,
    toggleSelection: () -> Unit
) {
    val modifier = Modifier.width(30.dp)

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

