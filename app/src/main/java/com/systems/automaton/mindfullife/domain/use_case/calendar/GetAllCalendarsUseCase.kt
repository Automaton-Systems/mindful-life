package com.systems.automaton.mindfullife.domain.use_case.calendar

import com.systems.automaton.mindfullife.domain.model.Calendar
import com.systems.automaton.mindfullife.domain.repository.CalendarRepository
import javax.inject.Inject

class GetAllCalendarsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(excluded: List<Int>): Map<String, List<Calendar>> {
        return calendarRepository.getCalendars().map { it.copy(included = (it.id.toInt() !in excluded)) }.groupBy { it.account }
    }
}