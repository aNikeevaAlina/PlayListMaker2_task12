package com.practicum.playlistmaker.createplaylist.domain

import android.net.Uri
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker.playlist.presentation.model.DetailedPlaylistModel
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(name: String, description: String? = null, cover: Uri? = null)

    suspend fun updatePlaylist(
        playlist: DetailedPlaylistModel,
        name: String,
        description: String? = null,
        cover: Uri? = null
    )

    fun getPlaylistsFlow(): Flow<List<PlaylistModel>>

    suspend fun getAllPlaylists(): List<PlaylistModel>

    suspend fun addTrackToPlaylist(track: Track, playlistId: Int)

    fun getPlaylistById(id: Int): Flow<DetailedPlaylistModel>

    suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: Int)

    suspend fun deletePlaylistById(playlist: DetailedPlaylistModel): Boolean
}