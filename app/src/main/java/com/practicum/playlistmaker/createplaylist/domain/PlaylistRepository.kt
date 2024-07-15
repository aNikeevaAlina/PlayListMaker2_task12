package com.practicum.playlistmaker.createplaylist.domain

import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlistModel: PlaylistModel)

    fun getPlaylistsFlow(): Flow<List<PlaylistModel>>
}