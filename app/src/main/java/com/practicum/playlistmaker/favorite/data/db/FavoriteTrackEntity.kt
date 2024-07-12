package com.practicum.playlistmaker.favorite.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteTrackEntity(
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Int,
    @PrimaryKey
    val trackId: String,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String,
    val releaseDate: String,
    val previewUrl: String
)
