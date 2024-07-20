package com.practicum.playlistmaker.playlist.presentation.model

import android.os.Parcelable
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailedPlaylistModel(
    val id: Int,
    val cover: String?,
    val name: String,
    val description: String?,
    val count: Int,
    val totalTime: Int,
    val trackList: List<Track>
): Parcelable {
    fun toSimpleModel() = PlaylistModel(
        id = id,
        name = name,
        description = description,
        cover = cover,
        trackList = trackList.map { it.trackId }
    )
}
