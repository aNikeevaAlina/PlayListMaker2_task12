package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.Track

sealed interface HistoryUiState{
    object Empty: HistoryUiState
    data class NotEmpty(val tracks: List<Track>): HistoryUiState
}