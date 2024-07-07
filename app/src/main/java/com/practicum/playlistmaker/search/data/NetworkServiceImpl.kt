package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.model.TrackSearchException
import com.practicum.playlistmaker.search.domain.NetworkService
import com.practicum.playlistmaker.search.domain.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkServiceImpl(private val itunesApi: ApiForItunes) : NetworkService {

    override suspend fun search(text: String): List<Track> {
        val response = itunesApi.search(text)
        val body = response.body()
        if (response.isSuccessful && body != null) {
            return body.results
        } else {
            throw TrackSearchException()
        }
    }
}