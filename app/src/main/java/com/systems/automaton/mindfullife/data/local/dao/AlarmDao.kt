package com.systems.automaton.mindfullife.data.local.dao

import androidx.room.*
import com.systems.automaton.mindfullife.domain.model.Alarm

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarms")
    suspend fun getAll(): List<Alarm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun delete(id: Int)

}