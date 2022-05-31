package com.systems.automaton.mindfullife.data.local.dao

import androidx.room.*
import com.systems.automaton.mindfullife.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: Int): Task

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :title || '%'")
    fun getTasksByTitle(title: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET is_completed = :completed WHERE id = :id")
    suspend fun updateCompleted(id: Int, completed: Boolean)

}