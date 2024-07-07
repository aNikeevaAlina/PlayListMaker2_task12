package com.practicum.playlistmaker.search.domain

class SearchInteractorImpl(
    private val networkService: NetworkService,
    private val searchHistory: SearchHistory
): SearchInteractor {

    override suspend fun searchTracks(query:String): List<Track> {
        return networkService.search(query)
    }

    override fun getHistory() = searchHistory.get()

    override fun addTrackToHistory(track: Track) = searchHistory.add(track)

    override fun clearHistory() = searchHistory.clear()
}