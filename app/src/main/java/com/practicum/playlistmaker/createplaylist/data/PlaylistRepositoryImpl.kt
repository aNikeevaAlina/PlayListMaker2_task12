package com.practicum.playlistmaker.createplaylist.data

import com.practicum.playlistmaker.addToPlaylist.data.PlaylistTrackDao
import com.practicum.playlistmaker.addToPlaylist.data.PlaylistTrackMapper
import com.practicum.playlistmaker.createplaylist.data.db.PlaylistDao
import com.practicum.playlistmaker.createplaylist.data.db.PlaylistEntity
import com.practicum.playlistmaker.createplaylist.domain.PlaylistRepository
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker.playlist.presentation.model.DetailedPlaylistModel
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
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

    override fun getPlaylistById(id: Int): Flow<DetailedPlaylistModel> {
        val playlistFlow = playlistDao.getPlaylistById(id)
        val tracksFlow = playlistTrackDao.getAllTracks()
        return playlistFlow
            .filterNotNull()
            .combine(tracksFlow) { playlist, trackList ->
                val filteredTrackList = trackList.filter { playlist.trackList.contains(it.trackId) }
                DetailedPlaylistModel(
                    playlist.id,
                    playlist.cover,
                    playlist.name,
                    playlist.description,
                    playlist.trackList.size,
                    (filteredTrackList.sumOf { it.trackTime } / 60_000),
                    filteredTrackList.reversed().map { playlistTrackMapper.mapFromEntity(it) }
                )
            }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {
        val trackList = playlistDao.getTrackListForPlaylist(playlistId).toMutableList()
        trackList.remove(trackId)
        playlistDao.updateTrackList(playlistId, trackList)
        val playlists = playlistDao.getAllPlaylists()
        val isUsed =checkIsTrackUsed(trackId, playlists)
        if (!isUsed) playlistTrackDao.deleteTrack(trackId)
    }

    override suspend fun deletePlaylistById(playlist: DetailedPlaylistModel): Boolean {
        playlistDao.deletePlaylistById(playlist.id)
        val playlists = playlistDao.getAllPlaylists()
        playlist.trackList.forEach {
            val isUsed = checkIsTrackUsed(it.trackId, playlists)
            if (!isUsed) playlistTrackDao.deleteTrack(it.trackId)
        }
        return true
    }

    private fun checkIsTrackUsed(trackId: String, playlists: List<PlaylistEntity>): Boolean {
        var isUsed = false
        for (i in playlists) {
            if (i.trackList.contains(trackId)) {
                isUsed = true
                break
            }
        }
        return isUsed
    }
}