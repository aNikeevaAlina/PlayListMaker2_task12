package com.practicum.playlistmaker.media.presentation

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.createplaylist.domain.PlaylistInteractor

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    val playlistsFlow = playlistInteractor.getPlaylistsFlow()
}