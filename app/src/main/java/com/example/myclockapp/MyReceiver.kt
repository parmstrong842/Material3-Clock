package com.example.myclockapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as MyClockApplication

        when(intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d("MyReceiver", "ACTION_BOOT_COMPLETED")
            }
            context.getString(R.string.action_dismiss) -> {
                goAsync {
                    val repo = app.container.alarmRepository
                    val alarm = repo.getItem(intent.getStringExtra(context.getString(R.string.alarm_id))!!) //TODO add better update method
                    repo.updateItem(alarm.copy(enabled = false))
                }
                NotificationDispatcher.cancelAlarmNotification(context)
            }
            context.getString(R.string.action_snooze) -> {
                Log.d("MyReceiver", "snooze")
                NotificationDispatcher.cancelAlarmNotification(context)
                goAsync {
                    val alarm = app.container.alarmRepository.getItem(intent.getStringExtra(context.getString(R.string.alarm_id))!!)
                    val snoozeAlarm = alarm.copy(minute = alarm.minute + 5)
                    AlarmScheduler.schedule(context, snoozeAlarm)
                }
            } else -> {
                Log.d("MyReceiver", "Alarm!!!")
                NotificationDispatcher.fireAlarmNotification(
                    context,
                    intent.getStringExtra(context.getString(R.string.alarm_id))!!,
                    intent.getStringExtra(context.getString(R.string.extra_name))!!,
                    intent.getStringExtra(context.getString(R.string.extra_time))!!
                )
            }
        }
    }

    private fun BroadcastReceiver.goAsync(
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob()).launch {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }
}