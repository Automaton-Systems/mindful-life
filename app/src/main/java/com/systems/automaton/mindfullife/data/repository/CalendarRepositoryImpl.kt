package com.systems.automaton.mindfullife.data.repository

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.systems.automaton.mindfullife.domain.model.Calendar
import com.systems.automaton.mindfullife.domain.model.CalendarEvent
import com.systems.automaton.mindfullife.domain.repository.CalendarRepository
import com.systems.automaton.mindfullife.util.calendar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class CalendarRepositoryImpl(private val context: Context) : CalendarRepository {

    override suspend fun getEvents(): List<CalendarEvent> {
        return withContext(Dispatchers.IO) {
            val projection: Array<String> = arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.ALL_DAY,
                CalendarContract.Events.CALENDAR_COLOR,
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.RRULE,
                CalendarContract.Events.DURATION,
            )

            val uri: Uri = CalendarContract.Events.CONTENT_URI
            val contentResolver = context.contentResolver
            val cur: Cursor? = contentResolver.query(
                uri,
                projection,
                "${CalendarContract.Events.DTSTART} > ? AND ${CalendarContract.Events.DELETED} = 0",
                // events today or in the future only
                arrayOf(System.currentTimeMillis().toString()),
                "${CalendarContract.Events.DTSTART} ASC"
            )

            val events: MutableList<CalendarEvent> = mutableListOf()
            if (cur != null) {
                while (cur.moveToNext()) {
                    val eventId: Long = cur.getLong(ID_INDEX)
                    val title: String = cur.getString(TITLE_INDEX) ?: continue
                    val description: String? = cur.getString(DESC_INDEX)
                    val start: Long = cur.getLong(START_INDEX)
                    val duration: String = cur.getString(EVENT_DURATION_INDEX) ?: ""
                    val end: Long = if (duration.isNotBlank()) duration.extractEndFromDuration(start) else cur.getLong(END_INDEX)
                    val location: String? = cur.getString(LOCATION_INDEX)
                    val allDay: Boolean = cur.getInt(ALL_DAY_INDEX) == 1
                    val color: Int = cur.getInt(COlOR_INDEX)
                    val calendarId: Long = cur.getLong(EVENT_CALENDAR_ID_INDEX)
                    val rrule: String = cur.getString(EVENT_RRULE_INDEX) ?: ""
                    val recurring: Boolean = rrule.isNotBlank()
                    val frequency: String = rrule.extractFrequency()

                    events.add(CalendarEvent(
                        id = eventId,
                        title = title,
                        description = description,
                        start = start,
                        end = end,
                        location = location,
                        allDay = allDay,
                        color = color,
                        calendarId = calendarId,
                        frequency = frequency,
                        recurring = recurring,
                    ))
                }
                cur.close()
                events
            }else
                emptyList()
        }
    }

    override suspend fun getCalendars() : List<Calendar>{
        return withContext(Dispatchers.IO){
            val projection = arrayOf(
                CalendarContract.Calendars._ID,                     // 0
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 1
                CalendarContract.Calendars.ACCOUNT_NAME, // 2
                CalendarContract.Calendars.ACCOUNT_TYPE, // 3
                CalendarContract.Calendars.CALENDAR_COLOR// 4

            )
            val uri = CalendarContract.Calendars.CONTENT_URI
            val contentResolver = context.contentResolver
            val cur = contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )
            val calendars: MutableList<Calendar> = mutableListOf()
            if (cur != null) {
                while (cur.moveToNext()) {

                    val calID: Long = cur.getLong(CALENDAR_ID_INDEX)
                    val calendarName: String = cur.getString(CALENDAR_NAME_INDEX)
                    val accountName: String = cur.getString(ACCOUNT_NAME_INDEX)
                    val color: Int = cur.getInt(CALENDAR_CALENDAR_COLOR_INDEX)

                    calendars.add(
                        Calendar(
                            calID,
                            calendarName,
                            accountName,
                            color
                        )
                    )
                }
                cur.close()
                calendars
            }else
                emptyList()
        }
    }

    override suspend fun addEvent(event: CalendarEvent) {
        withContext(Dispatchers.IO){
            val values = ContentValues().apply {
                put(CalendarContract.Events.CALENDAR_ID, event.calendarId)
                put(CalendarContract.Events.TITLE, event.title)
                put(CalendarContract.Events.DESCRIPTION, event.description)
                put(CalendarContract.Events.DTSTART, event.start)
                put(CalendarContract.Events.ALL_DAY, event.allDay)
                put(CalendarContract.Events.EVENT_LOCATION, event.location)
                if (event.recurring){
                    put(CalendarContract.Events.RRULE, event.getEventRRule())
                    put(CalendarContract.Events.DURATION, event.getEventDuration())
                } else {
                    put(CalendarContract.Events.DTEND, event.end)
                }
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            }
            context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        }
    }

    override suspend fun updateEvent(event: CalendarEvent) {
        withContext(Dispatchers.IO){
            val values = ContentValues().apply {
                put(CalendarContract.Events.CALENDAR_ID, event.calendarId)
                put(CalendarContract.Events.TITLE, event.title)
                put(CalendarContract.Events.DESCRIPTION, event.description)
                put(CalendarContract.Events.DTSTART, event.start)
                if (event.recurring){
                    val end: Long? = null
                    put(CalendarContract.Events.RRULE, event.getEventRRule())
                    put(CalendarContract.Events.DURATION, event.getEventDuration())
                    put(CalendarContract.Events.DTEND, end)
                }else {
                    val rule: String? = null
                    put(CalendarContract.Events.RRULE, rule)
                    put(CalendarContract.Events.DURATION, rule)
                    put(CalendarContract.Events.DTEND, event.end)
                }
                put(CalendarContract.Events.ALL_DAY, event.allDay)
                put(CalendarContract.Events.EVENT_LOCATION, event.location)
            }
            val updateUri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.id)
            context.contentResolver.update(updateUri, values, null, null)
        }
    }

    override suspend fun deleteEvent(event: CalendarEvent) {
        withContext(Dispatchers.IO){
            val updateUri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.id)
            context.contentResolver.delete(updateUri, null, null)
        }
    }

    companion object {
        private const val ID_INDEX = 0
        private const val TITLE_INDEX = 1
        private const val DESC_INDEX = 2
        private const val START_INDEX = 3
        private const val END_INDEX = 4
        private const val LOCATION_INDEX = 5
        private const val ALL_DAY_INDEX = 6
        private const val COlOR_INDEX = 7
        private const val EVENT_CALENDAR_ID_INDEX = 8
        private const val EVENT_RRULE_INDEX = 9
        private const val EVENT_DURATION_INDEX = 10

        private const val CALENDAR_ID_INDEX: Int = 0
        private const val CALENDAR_NAME_INDEX: Int = 1
        private const val ACCOUNT_NAME_INDEX: Int = 2
        private const val CALENDAR_CALENDAR_COLOR_INDEX: Int = 4
    }
}