package com.practicum.playlistmaker.main.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.media.presentation.MediaFragment
import com.practicum.playlistmaker.search.presentation.SearchFragment
import com.practicum.playlistmaker.settings.presentation.SettingsFragment
import com.practicum.playlistmaker2.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val searchButton = findViewById<Button>(R.id.button_search)
        searchButton.setOnClickListener {
            val displayIntent = Intent(this, SearchFragment::class.java)
            startActivity(displayIntent)
        }


        val mediaButton = findViewById<Button>(R.id.button_media)
        mediaButton.setOnClickListener {
            val displayIntent = Intent(this, MediaFragment::class.java)
            startActivity(displayIntent)
        }


        val buttonSetting = findViewById<Button>(R.id.button_setting)
        buttonSetting.setOnClickListener {
            val displayIntent = Intent(this, SettingsFragment::class.java)
            startActivity(displayIntent)
        }
    }
}