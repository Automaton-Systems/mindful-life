package com.systems.automaton.mindfullife.presentation.main

import com.systems.automaton.mindfullife.domain.model.Task


sealed class DashboardEvent {
    data class ReadPermissionChanged(val hasPermission: Boolean) : DashboardEvent()
    data class UpdateTask(val task: Task) : DashboardEvent()
    object InitAll : DashboardEvent()
}