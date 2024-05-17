package com.practicum.playlistmaker.domain

interface NetworkService {

    fun search(text: String, callback: TrackSearchCallback)
}