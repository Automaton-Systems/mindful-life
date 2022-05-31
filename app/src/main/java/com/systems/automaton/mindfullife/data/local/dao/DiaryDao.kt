package com.systems.automaton.mindfullife.data.local.dao

import androidx.room.*
import com.systems.automaton.mindfullife.domain.model.DiaryEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diary")
    fun getAllEntries(): Flow<List<DiaryEntry>>

    @Query("SELECT * FROM diary WHERE id = :id")
    suspend fun getEntry(id: Int): DiaryEntry

    @Query("SELECT * FROM diary WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    suspend fun getEntriesByTitle(query: String): List<DiaryEntry>

    @Insert
    suspend fun insertEntry(diary: DiaryEntry)

    @Update
    suspend fun updateEntry(diary: DiaryEntry)

    @Delete
    suspend fun deleteEntry(diary: DiaryEntry)

}