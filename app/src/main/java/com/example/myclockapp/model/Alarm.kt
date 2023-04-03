package com.example.myclockapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val enabled: Boolean = false,
    val time: String = "6:00AM",
    val hour: Int = 0,
    val minute: Int = 0,
    val sun: Boolean = false,
    val mon: Boolean = false,
    val tue: Boolean = false,
    val wed: Boolean = false,
    val thu: Boolean = false,
    val fri: Boolean = false,
    val sat: Boolean = false,
    val name: String = ""
)