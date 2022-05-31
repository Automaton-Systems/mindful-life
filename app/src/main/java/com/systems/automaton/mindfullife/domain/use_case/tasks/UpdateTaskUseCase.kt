package com.systems.automaton.mindfullife.domain.use_case.tasks

import android.content.Context
import com.systems.automaton.mindfullife.domain.model.Task
import com.systems.automaton.mindfullife.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val context: Context
) {
    suspend operator fun invoke(task: Task) {
        tasksRepository.updateTask(task)
        context.refreshTasksWidget()
    }
}