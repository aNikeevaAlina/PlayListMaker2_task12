package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.data.NetworkServiceImpl

class SearchTrackInteractor {

    private val networkService: NetworkService = NetworkServiceImpl()

    fun search(text: String, callback: TrackSearchCallback) {
        networkService.search(text, callback)
    }
}