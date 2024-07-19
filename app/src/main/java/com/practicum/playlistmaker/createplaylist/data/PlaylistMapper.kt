package com.practicum.playlistmaker.createplaylist.data

import com.practicum.playlistmaker.createplaylist.data.db.PlaylistEntity
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel

class PlaylistMapper {

    fun mapToEntity(model: PlaylistModel) = PlaylistEntity(
        id = model.id,
        name = model.name,
        description = model.description,
        cover = model.cover,
        trackList = model.trackList
    )

    fun mapFromEntity(entity: PlaylistEntity) = PlaylistModel(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        cover = entity.cover,
        trackList = entity.trackList
    )
}