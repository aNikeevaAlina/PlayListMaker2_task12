package com.practicum.playlistmaker.createplaylist.domain

import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel

interface PlaylistRepository {

    suspend fun addPlaylist(playlistModel: PlaylistModel)
}