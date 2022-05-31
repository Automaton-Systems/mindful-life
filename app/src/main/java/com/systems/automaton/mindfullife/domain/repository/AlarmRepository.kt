package com.systems.automaton.mindfullife.domain.repository

import com.systems.automaton.mindfullife.domain.model.Alarm

interface AlarmRepository {

    suspend fun getAlarms(): List<Alarm>

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarm(id: Int)
}