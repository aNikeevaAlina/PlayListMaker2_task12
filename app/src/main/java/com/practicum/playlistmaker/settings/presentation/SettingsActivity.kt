package com.practicum.playlistmaker.settings.presentation

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker2.R


class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        viewModel.nightModeState.observe(this) {
            themeSwitcher.isChecked = it
        }

        val shareTextView = findViewById<TextView>(R.id.share_app)
        shareTextView.setOnClickListener {
            viewModel.shareApp()
        }
        val supportTextView = findViewById<TextView>(R.id.support)
        supportTextView.setOnClickListener {
            viewModel.getSupport()
        }

        val agreeTextView = findViewById<TextView>(R.id.agree)
        agreeTextView.setOnClickListener {
            viewModel.agreement()
        }

        val backImageView = findViewById<ImageView>(R.id.back)
        backImageView.setOnClickListener {
            finish()
        }
    }
}