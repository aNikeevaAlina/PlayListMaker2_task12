package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.favorite.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.Track

class PlayerInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
): PlayerInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        favoriteTracksRepository.addTrackToFavorite(track)
    }

    override suspend fun removeTrackFromFavoritesById(trackId: String) {
        favoriteTracksRepository.removeTrackFromFavoritesById(trackId)
    }

    override suspend fun getAllFavoriteTracksIds(): List<String> {
        return favoriteTracksRepository.getAllFavoriteTracksIds()
    }
}