package com.practicum.playlistmaker.search.domain

interface NetworkService {

    suspend fun search(text: String): List<Track>
}