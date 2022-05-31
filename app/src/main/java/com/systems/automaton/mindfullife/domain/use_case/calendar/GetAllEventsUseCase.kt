package com.systems.automaton.mindfullife.domain.use_case.calendar

import com.systems.automaton.mindfullife.domain.model.CalendarEvent
import com.systems.automaton.mindfullife.domain.repository.CalendarRepository
import com.systems.automaton.mindfullife.util.date.formatDay
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(excluded: List<Int>): Map<String, List<CalendarEvent>> {
        return calendarRepository.getEvents()
            .filter { it.calendarId.toInt() !in excluded }
            .groupBy { event ->
                    event.start.formatDay()
            }
    }
}