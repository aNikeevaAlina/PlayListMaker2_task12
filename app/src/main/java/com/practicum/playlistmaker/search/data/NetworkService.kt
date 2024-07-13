package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface NetworkService {

    fun search(text: String): Flow<List<Track>>
}