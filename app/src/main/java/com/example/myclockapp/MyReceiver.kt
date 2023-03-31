package com.example.myclockapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as MyClockApplication

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("MyReceiver", "ACTION_BOOT_COMPLETED")


        }
        else {
            goAsync {
                Log.d("MyReceiver", "I'm working in thread ${Thread.currentThread().name}")

                val repo = app.container.alarmRepository
                val alarm = repo.getItem(intent.action?.toInt()!!)
                repo.updateItem(alarm.copy(enabled = false))
            }
            Log.d("MyReceiver", "Alarm!!!")
        }
    }

    private fun BroadcastReceiver.goAsync(
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob()).launch(Dispatchers.Default) {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }
}