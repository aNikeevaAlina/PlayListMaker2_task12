package com.practicum.playlistmaker.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
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
        searchDebounceJob?.cancel()
        lastSearchedText = text
        if (text.isBlank()) return
        searchDebounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTracks(text)
        }
    }

    fun searchTracks(query: String) {
        if (query.isEmpty()) return
        _searchState.value = SearchUiState.Loading
        viewModelScope.launch {
            interactor.searchTracks(query)
                .catch { _searchState.value = SearchUiState.Error(it.message.orEmpty()) }
                .collect { _searchState.value = SearchUiState.Success(it) }
        }
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