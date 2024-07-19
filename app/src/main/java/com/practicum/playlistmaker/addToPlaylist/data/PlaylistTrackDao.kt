package com.practicum.playlistmaker.addToPlaylist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.addToPlaylist.data.entity.PlaylistTrackDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: PlaylistTrackDbEntity)
    @Query("SELECT * FROM PlaylistTrackDbEntity")
    fun getAllTracks(): Flow<List<PlaylistTrackDbEntity>>
    @Query("DELETE FROM PlaylistTrackDbEntity WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)
}