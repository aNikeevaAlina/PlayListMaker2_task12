package com.practicum.playlistmaker.search.domain

class SearchInteractorImpl(
    private val networkService: NetworkService,
    private val searchHistory: SearchHistory
): SearchInteractor {

    override fun searchTracks(query:String, callback: TrackSearchCallback) {
        networkService.search(query, callback)
    }

    override fun getHistory() = searchHistory.get()

    override fun addTrackToHistory(track: Track) = searchHistory.add(track)

    override fun clearHistory() = searchHistory.clear()
}