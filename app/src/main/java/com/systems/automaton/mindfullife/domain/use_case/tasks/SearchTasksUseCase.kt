package com.systems.automaton.mindfullife.domain.use_case.tasks

import com.systems.automaton.mindfullife.domain.model.Task
import com.systems.automaton.mindfullife.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTasksUseCase @Inject constructor(
    private val tasksRepository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<Task>> {
        return tasksRepository.searchTasks(query)
    }
}