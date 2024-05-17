package com.practicum.playlistmaker.settings.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker2.R


class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        viewModel.nightModeState.observe(this) {
            themeSwitcher.isChecked = it
        }

        val shareTextView = findViewById<TextView>(R.id.share_app)
        shareTextView.setOnClickListener {
            Intent().apply {
                setAction(Intent.ACTION_SEND)
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.url_course_android_dev)
                )
                setType("text/plain")
                startActivity(this)
            }

        }
        val supportTextView = findViewById<TextView>(R.id.support)
        supportTextView.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "plain/text"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_developer)))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.subjectForDevelopers)
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.thanksForDevelopers)
                )
                startActivity(Intent.createChooser(this, ""))
            }
        }

        val agreeTextView = findViewById<TextView>(R.id.agree)
        agreeTextView.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.url_practicum_offer))
                startActivity(this)
            }
        }

        val backImageView = findViewById<ImageView>(R.id.back)
        backImageView.setOnClickListener {
            finish()
        }
    }
}