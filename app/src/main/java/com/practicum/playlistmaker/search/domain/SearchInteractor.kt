package com.practicum.playlistmaker.search.domain

interface SearchInteractor {

    suspend fun searchTracks(query: String): List<Track>

    fun getHistory(): Array<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistory()
}