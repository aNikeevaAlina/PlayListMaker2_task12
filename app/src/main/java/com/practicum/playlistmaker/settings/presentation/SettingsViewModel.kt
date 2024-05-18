package com.practicum.playlistmaker.settings.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.practicum.playlistmaker.App
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

    fun getShareIntent() = interactor.getShareAppIntent()

    fun getSupportIntent() = interactor.getSupportIntent()

    fun getAgreementIntent() = interactor.getAgreementIntent()

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                    return SettingsViewModel(
                        (application as App).provideSettingsInteractor()
                    ) as T
                }
            }
    }
}