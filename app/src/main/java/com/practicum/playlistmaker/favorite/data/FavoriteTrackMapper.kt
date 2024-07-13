package com.practicum.playlistmaker.favorite.data

import com.practicum.playlistmaker.favorite.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.search.domain.Track

class FavoriteTrackMapper {
    fun mapToEntity(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackName,
            track.artistName,
            track.artworkUrl100,
            track.trackTimeMillis,
            track.trackId,
            track.country,
            track.primaryGenreName,
            track.collectionName,
            track.releaseDate,
            track.previewUrl
        )
    }

    fun mapFromEntity(entity: FavoriteTrackEntity): Track {
        return Track(
            entity.trackName,
            entity.artistName,
            entity.artworkUrl100,
            entity.trackTimeMillis,
            entity.trackId,
            entity.country,
            entity.primaryGenreName,
            entity.collectionName,
            entity.releaseDate,
            entity.previewUrl,
            true
        )
    }
}