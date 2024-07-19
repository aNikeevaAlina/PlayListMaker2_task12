package com.practicum.playlistmaker.addToPlaylist.data

import com.practicum.playlistmaker.addToPlaylist.data.entity.PlaylistTrackDbEntity
import com.practicum.playlistmaker.search.domain.Track

class PlaylistTrackMapper {

    fun mapToEntity(track: Track) = PlaylistTrackDbEntity(
        trackName = track.trackName,
        artistName = track.artistName,
        artworkUrl = track.artworkUrl100,
        trackTime = track.trackTimeMillis,
        trackId = track.trackId,
        country = track.country,
        primaryGenreName = track.primaryGenreName,
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        previewUrl = track.previewUrl,
        isFavorite = track.isFavorite
    )

    fun mapFromEntity(entity: PlaylistTrackDbEntity) = Track(
        trackName = entity.trackName,
        artistName = entity.artistName,
        artworkUrl100 = entity.artworkUrl,
        trackTimeMillis = entity.trackTime,
        trackId = entity.trackId,
        country = entity.country,
        primaryGenreName = entity.primaryGenreName,
        collectionName = entity.collectionName,
        releaseDate = entity.releaseDate,
        previewUrl = entity.previewUrl,
        isFavorite = entity.isFavorite
    )
}