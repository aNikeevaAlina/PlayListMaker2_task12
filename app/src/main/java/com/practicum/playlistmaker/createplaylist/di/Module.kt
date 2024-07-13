package com.practicum.playlistmaker.createplaylist.di

import com.practicum.playlistmaker.createplaylist.presentation.CreatePlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createPlaylistModule = module {
    viewModelOf(::CreatePlaylistViewModel)
}