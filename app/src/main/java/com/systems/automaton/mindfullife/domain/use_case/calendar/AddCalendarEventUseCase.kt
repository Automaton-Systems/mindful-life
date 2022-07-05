package com.systems.automaton.mindfullife.domain.use_case.calendar

import android.content.Context
import android.content.Intent
import com.systems.automaton.mindfullife.domain.model.CalendarEvent
import com.systems.automaton.mindfullife.domain.repository.CalendarRepository
import com.systems.automaton.mindfullife.presentation.glance_widgets.RefreshCalendarWidgetReceiver
import javax.inject.Inject

class AddCalendarEventUseCase @Inject constructor(
    private val calendarEventRepository: CalendarRepository,
    private val context: Context
) {
    suspend operator fun invoke(calendarEvent: CalendarEvent) {
        calendarEventRepository.addEvent(calendarEvent)
        val updateIntent = Intent(context, RefreshCalendarWidgetReceiver::class.java)
        context.sendBroadcast(updateIntent)
    }

}