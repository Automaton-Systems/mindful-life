package com.systems.automaton.mindfullife.domain.use_case.tasks

import com.systems.automaton.mindfullife.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val tasksRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int) = tasksRepository.getTaskById(id)
}