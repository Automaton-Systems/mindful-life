package com.systems.automaton.mindfullife.domain.use_case.settings

import androidx.datastore.preferences.core.Preferences
import com.systems.automaton.mindfullife.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun <T> invoke(key: Preferences.Key<T>, defaultValue: T) = settingsRepository.getSettings(key, defaultValue)
}