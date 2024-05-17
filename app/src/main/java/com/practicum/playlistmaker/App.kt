package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.search.data.NetworkServiceImpl
import com.practicum.playlistmaker.search.data.SearchHistoryImpl
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class App : Application() {

    private lateinit var searchInteractor: SearchInteractor
    private lateinit var settingsInteractor: SettingsInteractor

    private fun isDarkMode(context: Context): Boolean {
        val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onCreate() {
        super.onCreate()
        val settingsPrefs = getSharedPreferences(SAVE_THEME, MODE_PRIVATE)
        val settingsRepository = SettingsRepositoryImpl(settingsPrefs, isDarkMode(this))
        val networkService = NetworkServiceImpl()
        val searchHistory = SearchHistoryImpl(getSharedPreferences(HISTORY_SEARCH, MODE_PRIVATE))
        searchInteractor = SearchInteractor(networkService, searchHistory)
        settingsInteractor = SettingsInteractor(settingsRepository)

        switchTheme(settingsRepository.getCurrentNightModeState())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun provideSearchInteractor() = searchInteractor
    fun provideSettingsInteractor() = settingsInteractor

    companion object {
        const val SAVE_THEME = "save_theme_file"
        const val HISTORY_SEARCH = "HISTORY_SEARCH"
    }
}