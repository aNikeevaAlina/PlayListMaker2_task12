package com.practicum.playlistmaker.settings.domain

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker2.R

class SettingsInteractor(
    private val settingsRepository: SettingsRepository,
    private val application: Application
) {

    fun getCurrentNightModeState() = settingsRepository.getCurrentNightModeState()

    fun setNightMode(isNight: Boolean) = settingsRepository.setNightMode(isNight)

    fun getShareAppIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                application.getString(R.string.url_course_android_dev)
            )
            type = "text/plain"
        }
    }

    fun getSupportIntent() : Intent {
        return  Intent(Intent.ACTION_SEND).apply {
            type = "plain/text"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(application.getString(R.string.email_developer)))
            putExtra(
                Intent.EXTRA_SUBJECT,
                application.getString(R.string.subjectForDevelopers)
            )
            putExtra(
                Intent.EXTRA_TEXT,
                application.getString(R.string.thanksForDevelopers)
            )
        }
    }

    fun getAgreementIntent(): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(application.getString(R.string.url_practicum_offer))
        }
    }
}