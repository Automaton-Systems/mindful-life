package com.systems.automaton.mindfullife.presentation.calendar

import com.systems.automaton.mindfullife.domain.model.Calendar

sealed class CalendarViewModelEvent {
    data class IncludeCalendar(val calendar: Calendar) : CalendarViewModelEvent()
    data class ReadPermissionChanged(val hasPermission: Boolean) : CalendarViewModelEvent()
}
