package com.example.myclockapp.data

import com.example.myclockapp.model.Alarm

class AlarmRepository {

    fun fetchAlarms(): List<Alarm> {
        return AlarmDummyData.fakeAlarmList
    }

    fun addAlarm(alarm: Alarm) {
        AlarmDummyData.fakeAlarmList.add(alarm)
    }
}

object AlarmDummyData {
    var id = 1;
    val fakeAlarmList = MutableList(3) {
        Alarm( id++,"6:00AM", true, true, true, true, true, true, true, "test alarm")
    }
}


