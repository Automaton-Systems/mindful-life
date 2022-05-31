package com.systems.automaton.mindfullife.domain.use_case.notes

import com.systems.automaton.mindfullife.domain.repository.NoteRepository
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
    private val notesRepository: NoteRepository
) {
    suspend operator fun invoke(query: String) = notesRepository.searchNotes(query)
}