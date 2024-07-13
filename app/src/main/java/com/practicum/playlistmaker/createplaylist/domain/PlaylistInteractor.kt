package com.practicum.playlistmaker.createplaylist.domain

import android.net.Uri

interface PlaylistInteractor {

    suspend fun addPlaylist(name: String, description: String? = null, cover: Uri? = null)
}