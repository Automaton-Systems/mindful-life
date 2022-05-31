package com.systems.automaton.mindfullife.domain.use_case.tasks

import android.content.Context
import com.systems.automaton.mindfullife.domain.model.Task
import com.systems.automaton.mindfullife.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val context: Context
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.deleteTask(task)
        context.refreshTasksWidget()
    }
}