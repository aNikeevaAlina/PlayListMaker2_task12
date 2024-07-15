package com.practicum.playlistmaker.createplaylist.domain.model

data class PlaylistModel(
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val cover: String? = null,
    val trackList: List<String> = emptyList(),
    val count: Int = trackList.size
)