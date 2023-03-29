package com.example.myclockapp.data

import com.example.myclockapp.model.Alarm

class AlarmRepository {

    fun fetchAlarms(): List<Alarm> {
        return AlarmDummyData.fakeAlarmList
    }

    fun fetchAlarm(id: Int): Alarm {
        return if (id == -1)
            Alarm()
        else
            AlarmDummyData.fakeAlarmList[id]
    }

    fun insertAlarm(alarm: Alarm) {
        AlarmDummyData.fakeAlarmList.add(alarm)
    }

    fun updateAlarm(id: Int, alarm: Alarm) {
        AlarmDummyData.fakeAlarmList[id] = alarm
    }
}

object AlarmDummyData {
    var id = 0;
    val fakeAlarmList = MutableList(3) {
        Alarm( id++,"6:00AM", true, true, true, true, true, true, true, "test alarm")
    }
}


