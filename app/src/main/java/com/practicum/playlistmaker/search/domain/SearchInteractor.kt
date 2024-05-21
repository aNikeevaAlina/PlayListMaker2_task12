package com.practicum.playlistmaker.search.domain

class SearchInteractor(
    private val networkService: NetworkService,
    private val searchHistory: SearchHistory
) {

    fun searchTracks(query:String, callback: TrackSearchCallback) {
        networkService.search(query, callback)
    }

    fun getHistory() = searchHistory.get()

    fun addTrackToHistory(track: Track) = searchHistory.add(track)

    fun clearHistory() = searchHistory.clear()
}