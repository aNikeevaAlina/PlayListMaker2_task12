package com.practicum.playlistmaker.player.presentation

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.presentation.model.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel: ViewModel() {

    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Initial)
     val playerStateFlow = _playerStateFlow.asStateFlow()

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    private var trackTimeJob: Job? = null

    fun startCount(mediaPlayer: MediaPlayer) {
        trackTimeJob?.cancel()
        trackTimeJob = viewModelScope.launch {
            while (isActive) {
                delay(TIME_UPDATE_DELAY)
                _playerStateFlow.value = PlayerState.InProgress(formatter.format(mediaPlayer.currentPosition))
            }
        }
    }

    fun stopCount() = trackTimeJob?.cancel()

    companion object {
        private const val TIME_UPDATE_DELAY = 1000L
    }
}