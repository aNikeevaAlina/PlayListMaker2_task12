package com.practicum.playlistmaker.media.domain

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    fun getFavoriteTracksFlow(): Flow<List<Track>>
}