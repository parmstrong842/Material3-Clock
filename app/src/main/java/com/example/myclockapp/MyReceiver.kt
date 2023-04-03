package com.example.myclockapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.myclockapp.ui.NotificationDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as MyClockApplication

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("MyReceiver", "ACTION_BOOT_COMPLETED")


        } else {
            goAsync {
                val repo = app.container.alarmRepository
                val alarm = repo.getItem(intent.action!!) //TODO add better update method
                repo.updateItem(alarm.copy(enabled = false))
            }
            Log.d("MyReceiver", "Alarm!!!")
            NotificationDispatcher.fireAlarmNotification(context)
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