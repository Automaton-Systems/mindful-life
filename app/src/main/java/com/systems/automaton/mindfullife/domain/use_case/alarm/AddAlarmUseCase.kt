package com.systems.automaton.mindfullife.domain.use_case.alarm

import android.app.AlarmManager
import android.content.Context
import com.systems.automaton.mindfullife.domain.model.Alarm
import com.systems.automaton.mindfullife.domain.repository.AlarmRepository
import com.systems.automaton.mindfullife.util.alarms.scheduleAlarm
import javax.inject.Inject

class AddAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val context: Context
) {
    suspend operator fun invoke(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.scheduleAlarm(alarm, context)
        alarmRepository.insertAlarm(alarm)
    }
}