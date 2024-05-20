package com.practicum.playlistmaker.settings.data

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.settings.domain.ExternalNavigator
import com.practicum.playlistmaker2.R

class ExternalNavigatorImpl(
    private val application: Application
): ExternalNavigator {

    override fun shareApp() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                application.getString(R.string.url_course_android_dev)
            )
            type = "text/plain"
        }
        runIntent(intent)
    }

    override fun getSupport() {
        val intent = Intent(Intent.ACTION_SEND).apply {
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
        runIntent(intent)
    }

    override fun openAgreement() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(application.getString(R.string.url_practicum_offer))
        }
        runIntent(intent)
    }

    private fun runIntent(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        application.startActivity(intent)
    }

}