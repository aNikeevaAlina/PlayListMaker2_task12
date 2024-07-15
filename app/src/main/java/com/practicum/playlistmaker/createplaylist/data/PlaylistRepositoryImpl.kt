package com.practicum.playlistmaker.createplaylist.data

import com.practicum.playlistmaker.addToPlaylist.data.PlaylistTrackDao
import com.practicum.playlistmaker.addToPlaylist.data.PlaylistTrackMapper
import com.practicum.playlistmaker.createplaylist.data.db.PlaylistDao
import com.practicum.playlistmaker.createplaylist.domain.PlaylistRepository
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val mapper: PlaylistMapper,
    private val playlistTrackDao: PlaylistTrackDao,
    private val playlistTrackMapper: PlaylistTrackMapper
) : PlaylistRepository {

    override suspend fun addPlaylist(playlistModel: PlaylistModel) {
        playlistDao.addPlaylist(mapper.mapToEntity(playlistModel))
    }

    override fun getPlaylistsFlow(): Flow<List<PlaylistModel>> {
        return playlistDao.getPlaylistsFlow().map { list -> list.map { mapper.mapFromEntity(it) } }
    }

    override suspend fun getAllPlaylists(): List<PlaylistModel> {
        return playlistDao.getAllPlaylists().map { mapper.mapFromEntity(it) }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
        playlistTrackDao.addTrack(playlistTrackMapper.mapToEntity(track))
        val trackList = playlistDao.getTrackListForPlaylist(playlistId).toMutableList()
        trackList.add(track.trackId)
        playlistDao.updateTrackList(playlistId, trackList)
    }
}