package com.systems.automaton.mindfullife.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.systems.automaton.mindfullife.domain.model.CalendarEvent
import com.systems.automaton.mindfullife.domain.model.DiaryEntry
import com.systems.automaton.mindfullife.domain.model.Task
import com.systems.automaton.mindfullife.domain.use_case.calendar.GetAllEventsUseCase
import com.systems.automaton.mindfullife.domain.use_case.diary.GetAllEntriesUseCase
import com.systems.automaton.mindfullife.domain.use_case.settings.GetSettingsUseCase
import com.systems.automaton.mindfullife.domain.use_case.tasks.GetAllTasksUseCase
import com.systems.automaton.mindfullife.domain.use_case.tasks.UpdateTaskUseCase
import com.systems.automaton.mindfullife.util.Constants
import com.systems.automaton.mindfullife.util.date.inTheLastWeek
import com.systems.automaton.mindfullife.util.settings.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSettings: GetSettingsUseCase,
    private val getAllTasks: GetAllTasksUseCase,
    private val getAllEntriesUseCase: GetAllEntriesUseCase,
    private val updateTask: UpdateTaskUseCase,
    private val getAllEventsUseCase: GetAllEventsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
    private set

    private var refreshTasksJob : Job? = null

    val themMode = getSettings(intPreferencesKey(Constants.SETTINGS_THEME_KEY), ThemeSettings.AUTO.value)
    val defaultStartUpScreen = getSettings(intPreferencesKey(Constants.DEFAULT_START_UP_SCREEN_KEY), StartUpScreenSettings.SPACES.value)

    fun onDashboardEvent(event: DashboardEvent) {
        when(event) {
            is DashboardEvent.ReadPermissionChanged -> {
                if (event.hasPermission)
                    getCalendarEvents()
            }
            is DashboardEvent.UpdateTask -> viewModelScope.launch {
                updateTask(event.task)
            }
            DashboardEvent.InitAll -> collectDashboardData()
        }
    }

    data class UiState(
        val dashBoardTasks: List<Task> = emptyList(),
        val dashBoardEvents: Map<String, List<CalendarEvent>> = emptyMap(),
        val summaryTasks: List<Task> = emptyList(),
        val dashBoardEntries: List<DiaryEntry> = emptyList()
    )

    private fun getCalendarEvents() = viewModelScope.launch {
        val excluded = getSettings(
            stringSetPreferencesKey(Constants.EXCLUDED_CALENDARS_KEY),
            emptySet()
        ).first()
        val events = getAllEventsUseCase(excluded.toIntList())
        uiState = uiState.copy(
            dashBoardEvents = events
        )
    }

    private fun collectDashboardData() = viewModelScope.launch {
        combine(
            getSettings(
                intPreferencesKey(Constants.TASKS_ORDER_KEY),
                Order.DateModified(OrderType.ASC()).toInt()
            ),
            getSettings(
                booleanPreferencesKey(Constants.SHOW_COMPLETED_TASKS_KEY),
                false
            ),
            getAllEntriesUseCase(Order.DateCreated(OrderType.ASC()))
        ) { order, showCompleted, entries ->
            uiState = uiState.copy(
                dashBoardEntries = entries,
            )
            refreshTasks(order.toOrder(), showCompleted)
        }.collect()
    }

    private fun refreshTasks(order: Order, showCompleted: Boolean) {
        refreshTasksJob?.cancel()
        refreshTasksJob = getAllTasks(order).onEach { tasks ->
                uiState = uiState.copy(
                    dashBoardTasks = if (showCompleted) tasks else tasks.filter { !it.isCompleted },
                    summaryTasks = tasks.filter { it.createdDate.inTheLastWeek() }
                )
            }.launchIn(viewModelScope)
    }

}