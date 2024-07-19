package com.practicum.playlistmaker.playlist.di

import com.practicum.playlistmaker.playlist.presentation.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val playlistModule = module {
    viewModelOf(::PlaylistViewModel)
}