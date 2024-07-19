package com.practicum.playlistmaker.createplaylist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val cover: String? = null,
    val trackList: List<String> = emptyList()
)