package com.systems.automaton.mindfullife.domain.use_case.notes

import com.systems.automaton.mindfullife.domain.repository.NoteRepository
import javax.inject.Inject

class GetAllNoteFoldersUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke() = repository.getAllNoteFolders()
}