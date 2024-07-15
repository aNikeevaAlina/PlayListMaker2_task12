package com.practicum.playlistmaker.favorite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.addToPlaylist.data.PlaylistTrackDao
import com.practicum.playlistmaker.addToPlaylist.data.entity.PlaylistTrackDbEntity
import com.practicum.playlistmaker.createplaylist.data.db.PlaylistDao
import com.practicum.playlistmaker.createplaylist.data.db.PlaylistEntity
import com.practicum.playlistmaker.createplaylist.data.db.StringListConverter
import com.practicum.playlistmaker.favorite.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.favorite.data.db.FavoriteTracksDao

@Database(
    entities = [FavoriteTrackEntity::class, PlaylistEntity::class, PlaylistTrackDbEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun favoriteTracksDao(): FavoriteTracksDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
}