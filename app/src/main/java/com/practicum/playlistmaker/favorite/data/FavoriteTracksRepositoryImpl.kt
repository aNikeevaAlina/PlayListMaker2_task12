package com.practicum.playlistmaker.favorite.data

import com.practicum.playlistmaker.favorite.data.db.FavoriteTracksDao
import com.practicum.playlistmaker.favorite.domain.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(
    private val dao: FavoriteTracksDao,
    private val favoriteTrackMapper: FavoriteTrackMapper
) : FavoriteTracksRepository {

    override suspend fun addTrackToFavorite(track: Track) {
        dao.addTrack(favoriteTrackMapper.mapToEntity(track))
    }

    override suspend fun removeTrackFromFavoritesById(trackId: String) {
        dao.removeTrackById(trackId)
    }

    override fun getFavoriteTracksFlow(): Flow<List<Track>> {
        return dao.getTracksFlow()
            .map { list -> list.map { favoriteTrackMapper.mapFromEntity(it) } }
    }

    override suspend fun getAllFavoriteTracksIds(): List<String> {
        return dao.getFavoriteTracksIds()
    }
}