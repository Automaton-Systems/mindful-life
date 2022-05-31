package com.systems.automaton.mindfullife.domain.use_case.notes

import com.systems.automaton.mindfullife.domain.model.Note
import com.systems.automaton.mindfullife.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val notesRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = notesRepository.updateNote(note)
}