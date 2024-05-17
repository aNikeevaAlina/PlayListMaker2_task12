package com.practicum.playlistmaker.settings.domain

class SettingsInteractor(
    private val settingsRepository: SettingsRepository
) {

    fun getCurrentNightModeState() = settingsRepository.getCurrentNightModeState()

    fun setNightMode(isNight: Boolean) = settingsRepository.setNightMode(isNight)
}