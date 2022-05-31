package com.systems.automaton.mindfullife.domain.repository

import com.systems.automaton.mindfullife.domain.model.Calendar
import com.systems.automaton.mindfullife.domain.model.CalendarEvent

interface CalendarRepository {

    suspend fun getEvents(): List<CalendarEvent>

    suspend fun getCalendars(): List<Calendar>
}