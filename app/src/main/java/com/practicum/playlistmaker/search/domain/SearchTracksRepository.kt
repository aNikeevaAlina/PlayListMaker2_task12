package com.practicum.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchTracksRepository {

    fun search(text: String): Flow<List<Track>>
}