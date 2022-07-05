package com.systems.automaton.mindfullife.domain.use_case.notes

import com.systems.automaton.mindfullife.domain.model.NoteFolder
import com.systems.automaton.mindfullife.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteFolderUseCass @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(folder: NoteFolder) = noteRepository.insertNoteFolder(folder)
}