package com.practicum.playlistmaker.settings.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker2.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val themeSwitcher = view.findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        viewModel.nightModeState.observe(viewLifecycleOwner) {
            themeSwitcher.isChecked = it
        }

        val shareTextView = view.findViewById<TextView>(R.id.share_app)
        shareTextView.setOnClickListener {
            viewModel.shareApp()
        }
        val supportTextView = view.findViewById<TextView>(R.id.support)
        supportTextView.setOnClickListener {
            viewModel.getSupport()
        }

        val agreeTextView = view.findViewById<TextView>(R.id.agree)
        agreeTextView.setOnClickListener {
            viewModel.agreement()
        }
    }
}