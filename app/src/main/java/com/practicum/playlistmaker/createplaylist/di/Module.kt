package com.practicum.playlistmaker.createplaylist.di

import com.practicum.playlistmaker.createplaylist.data.PlaylistMapper
import com.practicum.playlistmaker.createplaylist.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.createplaylist.data.db.PlaylistDao
import com.practicum.playlistmaker.createplaylist.domain.PlaylistInteractor
import com.practicum.playlistmaker.createplaylist.domain.PlaylistInteractorImpl
import com.practicum.playlistmaker.createplaylist.domain.PlaylistRepository
import com.practicum.playlistmaker.createplaylist.presentation.CreatePlaylistViewModel
import com.practicum.playlistmaker.favorite.data.db.AppDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val createPlaylistModule = module {
    viewModelOf(::CreatePlaylistViewModel)

    factory { PlaylistInteractorImpl(androidApplication(), get()) } bind PlaylistInteractor::class

    single { PlaylistRepositoryImpl(get(), get()) } bind PlaylistRepository::class

    factory { PlaylistMapper() }

    single { get<AppDataBase>().playlistDao() } bind PlaylistDao::class
}