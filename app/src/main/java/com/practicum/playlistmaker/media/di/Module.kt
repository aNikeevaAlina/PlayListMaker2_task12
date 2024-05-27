package com.practicum.playlistmaker.media.di

import com.practicum.playlistmaker.media.presentation.FavoritesViewModel
import com.practicum.playlistmaker.media.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mediaModule = module {
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::PlaylistsViewModel)
}