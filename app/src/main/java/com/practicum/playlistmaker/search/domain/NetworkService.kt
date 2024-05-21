package com.practicum.playlistmaker.search.domain

interface NetworkService {

    fun search(text: String, callback: TrackSearchCallback)
}