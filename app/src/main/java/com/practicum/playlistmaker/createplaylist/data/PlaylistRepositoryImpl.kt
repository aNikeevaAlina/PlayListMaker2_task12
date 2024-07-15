package com.practicum.playlistmaker.createplaylist.data

import com.practicum.playlistmaker.createplaylist.data.db.PlaylistDao
import com.practicum.playlistmaker.createplaylist.domain.PlaylistRepository
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val mapper: PlaylistMapper
): PlaylistRepository {

    override suspend fun addPlaylist(playlistModel: PlaylistModel) {
        playlistDao.addPlaylist(mapper.mapToEntity(playlistModel))
    }

    override fun getPlaylistsFlow(): Flow<List<PlaylistModel>> {
        return playlistDao.getPlaylistsFlow().map { list -> list.map { mapper.mapFromEntity(it) } }
    }
}