package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.data.NetworkServiceImpl

class SearchTrackInteractor {

    private val networkService: NetworkService = NetworkServiceImpl()

    fun search(text: String, callback: TrackSearchCallback) {
        networkService.search(text, callback)
    }
}