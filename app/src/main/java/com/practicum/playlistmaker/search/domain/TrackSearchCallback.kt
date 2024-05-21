package com.practicum.playlistmaker.search.domain

interface TrackSearchCallback {

    fun onSuccess(result: List<Track>)
    fun onError(message: String)
}