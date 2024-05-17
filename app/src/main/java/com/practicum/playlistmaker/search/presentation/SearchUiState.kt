package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.Track

sealed interface SearchUiState {
    data class Success(val tracks: List<Track>): SearchUiState
    data class Error(val message: String): SearchUiState
    object Loading: SearchUiState
}
