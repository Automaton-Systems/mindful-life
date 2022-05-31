package com.systems.automaton.mindfullife.presentation.settings

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.systems.automaton.mindfullife.domain.use_case.settings.GetSettingsUseCase
import com.systems.automaton.mindfullife.domain.use_case.settings.SaveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
) : ViewModel() {

    fun <T> getSettings(key: Preferences.Key<T>, defaultValue: T) : Flow<T> {
        return getSettingsUseCase(key, defaultValue)
    }

    fun <T> saveSettings(key: Preferences.Key<T>, value: T) {
        viewModelScope.launch {
            saveSettingsUseCase(key, value)
        }
    }

}