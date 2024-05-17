package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.search.data.NetworkServiceImpl
import com.practicum.playlistmaker.search.data.SearchHistoryImpl
import com.practicum.playlistmaker.search.domain.SearchHistory
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.presentation.SearchActivity

class App : Application() {

    var darkTheme = false
    private lateinit var searchInteractor: SearchInteractor

    fun isDarkMode(context: Context): Boolean {
        val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(SAVE_THEME, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(BOOL_KEY, isDarkMode(getApplicationContext()))
        switchTheme(darkTheme)
        val networkService = NetworkServiceImpl()
        val searchHistory = SearchHistoryImpl(getSharedPreferences(HISTORY_SEARCH, MODE_PRIVATE))
        searchInteractor = SearchInteractor(networkService, searchHistory)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPrefs = getSharedPreferences(SAVE_THEME, MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(BOOL_KEY, darkThemeEnabled).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun provideSearchInteractor() = searchInteractor

    companion object {
        const val SAVE_THEME = "save_theme_file"
        const val BOOL_KEY = "key_for_bool"
        const val HISTORY_SEARCH = "HISTORY_SEARCH"
    }

}