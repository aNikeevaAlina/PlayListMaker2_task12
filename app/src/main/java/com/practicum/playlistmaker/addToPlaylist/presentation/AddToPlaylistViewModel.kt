package com.practicum.playlistmaker.addToPlaylist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.createplaylist.domain.PlaylistInteractor
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AddToPlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {

    val playlistsFlow = interactor.getPlaylistsFlow()

    private val _trackAddingResultFlow = MutableLiveData<PlaylistState>()
    val trackAddingResultFlow: LiveData<PlaylistState> get() = _trackAddingResultFlow

    fun addTrackToPlaylist(track: Track, playlistId: Int) {
        viewModelScope.launch {
            val currentPlaylist = playlistsFlow.firstOrNull()?.find { it.id == playlistId } ?: return@launch
            if (currentPlaylist.trackList.contains(track.trackId))
                _trackAddingResultFlow.value = PlaylistState.Unavailable(currentPlaylist.name)
            else {
                interactor.addTrackToPlaylist(track, playlistId)
                _trackAddingResultFlow.value = PlaylistState.Success(currentPlaylist.name)
            }
        }
    }
}