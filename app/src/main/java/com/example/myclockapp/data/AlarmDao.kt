package com.example.myclockapp.data

import androidx.room.*
import com.example.myclockapp.model.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Query("SELECT * from alarms")
    fun getAllItems(): Flow<List<Alarm>>

    @Query("SELECT * from alarms WHERE id = :id")
    suspend fun getItem(id: Int): Alarm

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Alarm)

    @Update
    suspend fun update(item: Alarm)

    @Delete
    suspend fun delete(item: Alarm)
}