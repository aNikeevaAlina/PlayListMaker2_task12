package com.practicum.playlistmaker.media.presentation

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.domain.FavoriteTracksInteractor

class FavoritesViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
): ViewModel() {

    val favoriteTracksFlow = favoriteTracksInteractor.getFavoriteTracksFlow()
}