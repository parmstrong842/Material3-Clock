package com.example.myclockapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.myclockapp.model.Alarm
import java.util.*

object AlarmScheduler {

    fun schedule(context: Context, alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra(context.getString(R.string.alarm_id), alarm.id)
            putExtra(context.getString(R.string.extra_name), alarm.name)
            putExtra(context.getString(R.string.extra_time), alarm.time)
        }
        val alarmPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT)

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
        calendar.set(Calendar.MINUTE, alarm.minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
        }

        val mHandler = Handler(Looper.getMainLooper())
        val showToast = Runnable {
            val toastText = String.format("Alarm is scheduled at %02d:%02d", alarm.hour, alarm.minute)
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }
        mHandler.post(showToast)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() , //TODO don't forget about this
            alarmPendingIntent
        )
    }

    fun unschedule(context: Context, alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyReceiver::class.java).apply {
            action = alarm.id
        }
        val alarmPendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(alarmPendingIntent)
    }
}