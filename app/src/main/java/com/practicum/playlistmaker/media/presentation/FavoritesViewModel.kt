package com.practicum.playlistmaker.media.presentation

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.favorite.domain.FavoriteTracksRepository

class FavoritesViewModel(
    private val favoriteTracksRepository: FavoriteTracksRepository
): ViewModel() {

    val favoriteTracksFlow = favoriteTracksRepository.getFavoriteTracksFlow()
}