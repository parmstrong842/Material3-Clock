package com.example.myclockapp.model

// time - hour, minutes, am/pm
// days of week
// alarm name

data class Alarm(
    val id: Int = 0,
    val time: String = "6:00AM",
    val sun: Boolean = false,
    val mon: Boolean = false,
    val tue: Boolean = false,
    val wed: Boolean = false,
    val thu: Boolean = false,
    val fri: Boolean = false,
    val sat: Boolean = false,
    val name: String = ""
)