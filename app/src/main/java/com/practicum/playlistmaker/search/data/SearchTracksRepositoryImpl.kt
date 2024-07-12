package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.favorite.data.db.FavoriteTracksDao
import com.practicum.playlistmaker.search.domain.SearchTracksRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksRepositoryImpl(
    private val networkService: NetworkService,
    private val favoriteTracksDao: FavoriteTracksDao
) : SearchTracksRepository {
    override fun search(text: String): Flow<List<Track>> {
        return networkService.search(text).map { list ->
            list.map {
                val favoriteTracksIds = favoriteTracksDao.getFavoriteTracksIds()
                it.copy(isFavorite = favoriteTracksIds.contains(it.trackId))
            }
        }
    }
}