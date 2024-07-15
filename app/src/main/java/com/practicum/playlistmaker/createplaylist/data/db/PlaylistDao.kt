package com.practicum.playlistmaker.createplaylist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM PlaylistEntity WHERE id = :id")
    suspend fun deletePlaylistById(id: Int)

    @Query("UPDATE PlaylistEntity SET trackList = :trackList WHERE id = :playlistId")
    suspend fun updateTrackList(playlistId: String, trackList: List<String>)

    @Query("SELECT trackList FROM PlaylistEntity WHERE id = :playlistId")
    suspend fun getTrackListForPlaylist(playlistId: Int): List<String>

    @Query("SELECT * FROM PlaylistEntity")
    fun getPlaylistsFlow(): Flow<List<PlaylistEntity>>
}