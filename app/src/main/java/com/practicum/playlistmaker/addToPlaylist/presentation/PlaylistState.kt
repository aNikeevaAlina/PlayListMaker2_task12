package com.practicum.playlistmaker.addToPlaylist.presentation

sealed class PlaylistState {
    class Success(val playlistName: String): PlaylistState()
    class Unavailable(val playlistName: String): PlaylistState()
}