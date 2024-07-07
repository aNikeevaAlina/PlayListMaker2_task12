package com.practicum.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val networkService: NetworkService,
    private val searchHistory: SearchHistory
): SearchInteractor {

    override fun searchTracks(query:String): Flow< List<Track>> {
        return networkService.search(query)
    }

    override fun getHistory() = searchHistory.get()

    override fun addTrackToHistory(track: Track) = searchHistory.add(track)

    override fun clearHistory() = searchHistory.clear()
}