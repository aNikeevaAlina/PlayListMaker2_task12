package com.practicum.playlistmaker.createplaylist.domain

import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlistModel: PlaylistModel)

    fun getPlaylistsFlow(): Flow<List<PlaylistModel>>

    suspend fun getAllPlaylists(): List<PlaylistModel>
    suspend fun addTrackToPlaylist(track: Track, playlistId: Int)

}