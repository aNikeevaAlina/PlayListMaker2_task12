package com.practicum.playlistmaker.favorite.di

import androidx.room.Room
import com.practicum.playlistmaker.favorite.data.db.AppDataBase
import com.practicum.playlistmaker.favorite.data.db.FavoriteTracksDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import org.koin.dsl.module

val favoriteTracksModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDataBase::class.java,
            "app_database"
        ).build()
    }

    single { get<AppDataBase>().favoriteTracksDao() } bind FavoriteTracksDao::class
}