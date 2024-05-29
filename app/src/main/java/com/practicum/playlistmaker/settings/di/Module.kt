package com.practicum.playlistmaker.settings.di

import android.app.Application
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.settings.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.ExternalNavigator
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::SettingsViewModel)

    factory<SettingsInteractor> { SettingsInteractorImpl(get(), get()) }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            androidApplication().getSharedPreferences(
                App.HISTORY_SEARCH,
                Application.MODE_PRIVATE
            ),
            androidApplication()
        )
    }

    factory<ExternalNavigator> { ExternalNavigatorImpl(androidApplication()) }
}