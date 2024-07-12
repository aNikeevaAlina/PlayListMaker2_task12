package com.practicum.playlistmaker.player.presentation

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorite.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.player.presentation.model.PlayerScreenState
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val favoriteTracksRepository: FavoriteTracksRepository
): ViewModel() {

    private val _playerStateFlow = MutableStateFlow<PlayerScreenState>(PlayerScreenState.TrackTime.Initial)
     val playerStateFlow = _playerStateFlow.asStateFlow()

    private val _trackStateFlow = MutableStateFlow<Track?>(null)
    val trackStateFlow = _trackStateFlow.asStateFlow().filterNotNull()

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    private var trackTimeJob: Job? = null

    fun setTrack(track: Track) {
        _trackStateFlow.value = track
    }

    fun onFavoriteClicked() {
        val currentTrack = _trackStateFlow.value ?: return
        viewModelScope.launch {
            if (currentTrack.isFavorite) {
                favoriteTracksRepository.removeTrackFromFavoritesById(currentTrack.trackId)
                _trackStateFlow.value = currentTrack.copy(isFavorite = false)
            } else {
                favoriteTracksRepository.addTrackToFavorite(currentTrack)
                _trackStateFlow.value = currentTrack.copy(isFavorite = true)
            }
        }
    }


    fun startCount(mediaPlayer: MediaPlayer) {
        trackTimeJob?.cancel()
        trackTimeJob = viewModelScope.launch {
            while (isActive) {
                delay(TIME_UPDATE_DELAY)
                _playerStateFlow.value = PlayerScreenState.TrackTime.InProgress(formatter.format(mediaPlayer.currentPosition))
            }
        }
    }

    fun resetTrackTime() {
        _playerStateFlow.value = PlayerScreenState.TrackTime.Initial
    }

    fun stopCount() = trackTimeJob?.cancel()

    companion object {
        private const val TIME_UPDATE_DELAY = 300L
    }
}