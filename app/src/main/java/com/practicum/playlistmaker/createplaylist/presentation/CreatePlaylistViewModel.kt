package com.practicum.playlistmaker.createplaylist.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.createplaylist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun createPlaylist(name: String, description: String? = null, cover: Uri? = null) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(name, description, cover)
        }
    }
}