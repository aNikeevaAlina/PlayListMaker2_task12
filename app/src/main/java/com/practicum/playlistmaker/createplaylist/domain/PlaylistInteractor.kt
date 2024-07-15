package com.practicum.playlistmaker.createplaylist.domain

import android.net.Uri
import com.practicum.playlistmaker.createplaylist.data.db.PlaylistEntity
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(name: String, description: String? = null, cover: Uri? = null)

    fun getPlaylistsFlow(): Flow<List<PlaylistModel>>
}