package com.practicum.playlistmaker.favorite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.favorite.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.favorite.data.db.FavoriteTracksDao

@Database(entities = [FavoriteTrackEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
}