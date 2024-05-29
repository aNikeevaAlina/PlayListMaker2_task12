package com.practicum.playlistmaker.settings.domain

interface SettingsInteractor {

    fun getCurrentNightModeState(): Boolean

    fun setNightMode(isNight: Boolean)

    fun shareApp()

    fun getSupport()

    fun agreement()
}