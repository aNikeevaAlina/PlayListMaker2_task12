package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TrackSearchCallback
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: SearchInteractor
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchUiState>()
    val searchState: LiveData<SearchUiState> = _searchState

    private val _historyState = MutableLiveData<HistoryUiState>()
    val historyState: LiveData<HistoryUiState> = _historyState

    private var searchDebounceJob: Job? = null
    private var lastSearchedText: String = ""

    init {
        val history = interactor.getHistory()
        _historyState.value = if (history.isEmpty()) HistoryUiState.Empty
        else HistoryUiState.NotEmpty(history.toList())
    }

    fun searchDebounce(text: String) {
        if (lastSearchedText == text) return
        lastSearchedText = text
        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTracks(text)
        }
    }

    fun searchTracks(query: String) {
        if (query.isEmpty()) return
        _searchState.value = SearchUiState.Loading
        interactor.searchTracks(query, object : TrackSearchCallback {
            override fun onSuccess(result: List<Track>) {
                _searchState.value = SearchUiState.Success(result)
            }

            override fun onError(message: String) {
                _searchState.value = SearchUiState.Error(message)
            }
        })
    }

    fun addTrackToHistory(track: Track) {
        interactor.addTrackToHistory(track)
        _historyState.value = HistoryUiState.NotEmpty(interactor.getHistory().toList())
    }

    fun clearHistory() {
        interactor.clearHistory()
        _historyState.value = HistoryUiState.Empty
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}