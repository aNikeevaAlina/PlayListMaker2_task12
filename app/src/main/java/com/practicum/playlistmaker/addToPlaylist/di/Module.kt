package com.practicum.playlistmaker.addToPlaylist.di

import com.practicum.playlistmaker.addToPlaylist.data.PlaylistTrackDao
import com.practicum.playlistmaker.addToPlaylist.data.PlaylistTrackMapper
import com.practicum.playlistmaker.addToPlaylist.presentation.AddToPlaylistViewModel
import com.practicum.playlistmaker.favorite.data.db.AppDataBase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val playlistSmallModule = module {
    factory {
        get<AppDataBase>().playlistTrackDao()
    } bind PlaylistTrackDao::class
    viewModelOf(::AddToPlaylistViewModel)

    factory { PlaylistTrackMapper() }
}