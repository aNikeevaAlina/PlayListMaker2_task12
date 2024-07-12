package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.search.domain.Track

interface PlayerInteractor {
    suspend fun addTrackToFavorite(track: Track)
    suspend fun removeTrackFromFavoritesById(trackId: String)
    suspend fun getAllFavoriteTracksIds(): List<String>
}