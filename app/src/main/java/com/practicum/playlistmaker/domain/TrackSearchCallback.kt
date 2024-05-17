package com.practicum.playlistmaker.domain

interface TrackSearchCallback {

    fun onSuccess(result: List<Track>)
    fun onError(message: String)
}