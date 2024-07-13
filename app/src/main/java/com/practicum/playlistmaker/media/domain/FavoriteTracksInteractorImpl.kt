package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.favorite.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
): FavoriteTracksInteractor {
    override fun getFavoriteTracksFlow(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracksFlow()
    }
}