package com.practicum.playlistmaker.settings.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(
    private val interactor: SettingsInteractor
) : ViewModel() {

    private val _nightModeState = MutableLiveData<Boolean>()
    val nightModeState: LiveData<Boolean> = _nightModeState

    init {
        val currentState = interactor.getCurrentNightModeState()
        _nightModeState.value = currentState
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        interactor.setNightMode(darkThemeEnabled)
        _nightModeState.value = darkThemeEnabled
    }

    fun shareApp() = interactor.shareApp()

    fun getSupport() = interactor.getSupport()

    fun agreement() = interactor.agreement()

}