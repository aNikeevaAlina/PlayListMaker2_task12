package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.createplaylist.di.createPlaylistModule
import com.practicum.playlistmaker.favorite.di.favoriteTracksModule
import com.practicum.playlistmaker.media.di.mediaModule
import com.practicum.playlistmaker.player.di.playerModule
import com.practicum.playlistmaker.search.di.searchModule
import com.practicum.playlistmaker.settings.di.settingsModule
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    searchModule,
                    settingsModule,
                    mediaModule,
                    playerModule,
                    favoriteTracksModule,
                    createPlaylistModule
                )
            )
        }

        val settingsRepository: SettingsRepository by inject()

        switchTheme(settingsRepository.getCurrentNightModeState())
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val SAVE_THEME = "save_theme_file"
        const val HISTORY_SEARCH = "HISTORY_SEARCH"
    }
}