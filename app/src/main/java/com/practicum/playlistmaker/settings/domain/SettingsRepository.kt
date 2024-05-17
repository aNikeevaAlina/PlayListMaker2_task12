package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {

    fun getCurrentNightModeState(): Boolean

    fun setNightMode(isNight: Boolean)
}