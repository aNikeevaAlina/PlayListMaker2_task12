package com.practicum.playlistmaker.favorite.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(trackEntity: FavoriteTrackEntity)

    @Query("DELETE FROM FavoriteTrackEntity WHERE trackId = :trackId")
    suspend fun removeTrackById(trackId: String)

    @Query("SELECT * FROM FavoriteTrackEntity")
    fun getTracksFlow(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT trackId FROM FavoriteTrackEntity")
    suspend fun getFavoriteTracksIds(): List<String>
}