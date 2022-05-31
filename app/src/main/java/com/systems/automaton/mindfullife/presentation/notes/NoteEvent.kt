package com.systems.automaton.mindfullife.presentation.notes

import com.systems.automaton.mindfullife.domain.model.Note
import com.systems.automaton.mindfullife.util.settings.ItemView
import com.systems.automaton.mindfullife.util.settings.Order

sealed class NoteEvent {
    data class GetNote(val noteId: Int) : NoteEvent()
    data class AddNote(val note: Note) : NoteEvent()
    data class SearchNotes(val query: String) : NoteEvent()
    data class UpdateOrder(val order: Order) : NoteEvent()
    data class UpdateView(val view: ItemView) : NoteEvent()
    data class UpdateNote(val note: Note) : NoteEvent()
    data class DeleteNote(val note: Note) : NoteEvent()
    object PinNote : NoteEvent()
    object ToggleReadingMode : NoteEvent()
    object ErrorDisplayed: NoteEvent()
}