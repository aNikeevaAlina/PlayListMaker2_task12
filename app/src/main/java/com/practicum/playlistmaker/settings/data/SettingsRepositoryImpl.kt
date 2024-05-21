package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val systemNightMode: Boolean
) : SettingsRepository {

    override fun getCurrentNightModeState(): Boolean {
        return sharedPrefs.getBoolean(BOOL_KEY, systemNightMode)
    }

    override fun setNightMode(isNight: Boolean) {
        sharedPrefs.edit().putBoolean(BOOL_KEY, isNight).apply()
    }

    companion object {
        const val BOOL_KEY = "key_for_bool"
    }
}