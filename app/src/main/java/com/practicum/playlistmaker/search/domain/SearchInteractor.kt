package com.practicum.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchTracks(query: String): Flow<List<Track>>

    fun getHistory(): Array<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistory()
}