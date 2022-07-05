package com.systems.automaton.mindfullife.presentation.calendar

import com.systems.automaton.mindfullife.domain.model.Calendar
import com.systems.automaton.mindfullife.domain.model.CalendarEvent

sealed class CalendarViewModelEvent {
    data class IncludeCalendar(val calendar: Calendar) : CalendarViewModelEvent()
    data class ReadPermissionChanged(val hasPermission: Boolean) : CalendarViewModelEvent()
    data class EditEvent(val event: CalendarEvent) : CalendarViewModelEvent()
    data class DeleteEvent(val event: CalendarEvent) : CalendarViewModelEvent()
    data class AddEvent(val event: CalendarEvent) : CalendarViewModelEvent()
    object ErrorDisplayed : CalendarViewModelEvent()
}
