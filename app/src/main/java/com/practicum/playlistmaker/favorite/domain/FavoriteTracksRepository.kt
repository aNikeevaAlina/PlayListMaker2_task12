package com.practicum.playlistmaker.favorite.domain

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addTrackToFavorite(track: Track)

    suspend fun removeTrackFromFavoritesById(trackId: String)

    fun getFavoriteTracksFlow(): Flow<List<Track>>

    suspend fun getAllFavoriteTracksIds(): List<String>
}