package com.practicum.playlistmaker.search.domain

interface SearchInteractor {

    fun searchTracks(query: String, callback: TrackSearchCallback)

    fun getHistory(): Array<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistory()
}