package com.practicum.playlistmaker.player.presentation.model

sealed interface PlayerScreenState {

    sealed class TrackTime: PlayerScreenState {
        abstract val currentTime: String
        object Initial: TrackTime() {
            override val currentTime = "00:00"
        }
        class InProgress(override val currentTime: String): TrackTime()
    }

}