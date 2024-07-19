package com.practicum.playlistmaker.editPlaylist.presentation

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.createplaylist.domain.PlaylistInteractor
import com.practicum.playlistmaker.createplaylist.presentation.CreatePlaylistViewModel
import com.practicum.playlistmaker.playlist.presentation.model.DetailedPlaylistModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val interactor: PlaylistInteractor) :
    CreatePlaylistViewModel(interactor) {

    fun saveUpdates(
        playlist: DetailedPlaylistModel,
        name: String,
        description: String? = null,
        cover: Uri? = null
    ) {
        viewModelScope.launch {
            interactor.updatePlaylist(playlist, name, description, cover)
        }
    }
}