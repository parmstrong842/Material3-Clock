package com.example.myclockapp.data

import com.example.myclockapp.model.Alarm
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmDao: AlarmDao) {
    fun getAllItems(): Flow<List<Alarm>> = alarmDao.getAllItems()

    suspend fun getItem(id: Int): Alarm = alarmDao.getItem(id)

    suspend fun insertItem(item: Alarm) = alarmDao.insert(item)

    suspend fun deleteItem(item: Alarm) = alarmDao.delete(item)

    suspend fun updateItem(item: Alarm) = alarmDao.update(item)
}


