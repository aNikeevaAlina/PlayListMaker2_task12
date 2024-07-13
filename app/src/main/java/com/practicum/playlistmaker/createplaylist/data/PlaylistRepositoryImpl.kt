package com.practicum.playlistmaker.createplaylist.data

import com.practicum.playlistmaker.createplaylist.data.db.PlaylistDao
import com.practicum.playlistmaker.createplaylist.domain.PlaylistRepository
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val mapper: PlaylistMapper
): PlaylistRepository {

    override suspend fun addPlaylist(playlistModel: PlaylistModel) {
        playlistDao.addPlaylist(mapper.mapToEntity(playlistModel))
    }
}