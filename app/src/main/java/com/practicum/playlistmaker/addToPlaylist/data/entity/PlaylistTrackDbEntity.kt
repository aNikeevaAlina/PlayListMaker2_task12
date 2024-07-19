package com.practicum.playlistmaker.addToPlaylist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistTrackDbEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: Int,
    val artworkUrl: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String,
    val previewUrl: String,
    var isFavorite: Boolean = false
)