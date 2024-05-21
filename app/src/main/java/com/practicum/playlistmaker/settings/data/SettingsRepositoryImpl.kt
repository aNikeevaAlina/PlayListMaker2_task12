package com.practicum.playlistmaker.settings.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val application: Application
) : SettingsRepository {

    override fun getCurrentNightModeState(): Boolean {
        return sharedPrefs.getBoolean(BOOL_KEY, isDarkMode(application))
    }

    override fun setNightMode(isNight: Boolean) {
        sharedPrefs.edit().putBoolean(BOOL_KEY, isNight).apply()
    }

    private fun isDarkMode(context: Context): Boolean {
        val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        const val BOOL_KEY = "key_for_bool"
    }
}