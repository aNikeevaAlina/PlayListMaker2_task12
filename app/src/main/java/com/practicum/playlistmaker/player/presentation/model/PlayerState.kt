package com.practicum.playlistmaker.player.presentation.model

sealed interface PlayerState {
    object Initial: PlayerState
    class InProgress(val currentTime: String): PlayerState
}