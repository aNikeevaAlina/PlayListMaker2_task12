package com.practicum.playlistmaker.settings.domain

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
    private val externalNavigator: ExternalNavigator
) : SettingsInteractor {

    override fun getCurrentNightModeState() = settingsRepository.getCurrentNightModeState()

    override fun setNightMode(isNight: Boolean) = settingsRepository.setNightMode(isNight)

    override fun shareApp() = externalNavigator.shareApp()

    override fun getSupport() = externalNavigator.getSupport()

    override fun agreement() = externalNavigator.openAgreement()
}