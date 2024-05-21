package com.practicum.playlistmaker.settings.domain

class SettingsInteractor(
    private val settingsRepository: SettingsRepository,
    private val externalNavigator: ExternalNavigator
) {

    fun getCurrentNightModeState() = settingsRepository.getCurrentNightModeState()

    fun setNightMode(isNight: Boolean) = settingsRepository.setNightMode(isNight)

    fun shareApp() = externalNavigator.shareApp()

    fun getSupport() = externalNavigator.getSupport()

    fun agreement() = externalNavigator.openAgreement()
}