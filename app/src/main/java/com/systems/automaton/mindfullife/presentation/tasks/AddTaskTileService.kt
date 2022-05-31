package com.systems.automaton.mindfullife.presentation.tasks

import android.content.Intent
import android.service.quicksettings.TileService
import androidx.core.net.toUri
import com.systems.automaton.mindfullife.presentation.main.MainActivity
import com.systems.automaton.mindfullife.util.Constants

class AddTaskTileService: TileService() {

    override fun onClick() {
        super.onClick()
        val intent = Intent(
            Intent.ACTION_VIEW,
            "${Constants.TASKS_SCREEN_URI}/true".toUri(),
            this,
            MainActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityAndCollapse(intent)
    }
}