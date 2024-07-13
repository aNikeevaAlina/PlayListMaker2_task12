package com.practicum.playlistmaker.main.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.media.presentation.MediaFragment
import com.practicum.playlistmaker.search.presentation.SearchFragment
import com.practicum.playlistmaker.settings.presentation.SettingsFragment
import com.practicum.playlistmaker2.R

class MainActivity : AppCompatActivity() {

    private val bottomNavigation by lazy { findViewById<BottomNavigationView>(R.id.bottomNavigation) }
    private val separatorView by lazy { findViewById<View>(R.id.separator) }

    private lateinit var navController: NavController

    private val fragmentsWithoutBottomNav = listOf(R.id.playerFragment)

    private val destinationListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            bottomNavigation.isVisible = destination.id !in fragmentsWithoutBottomNav
            separatorView.isVisible = destination.id !in fragmentsWithoutBottomNav
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener(destinationListener)

        bottomNavigation.setupWithNavController(navController)

    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(destinationListener)
    }
}