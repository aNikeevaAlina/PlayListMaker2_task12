package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.model.TrackSearchException
import com.practicum.playlistmaker.search.domain.NetworkService
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkServiceImpl(private val itunesApi: ApiForItunes) : NetworkService {

    override fun search(text: String): Flow<List<Track>> = flow {
        val response = itunesApi.search(text)
        val body = response.body()
        if (response.isSuccessful) {
            if (body != null) emit(body.results)
            else throw TrackSearchException("The data is null")
        } else {
            throw TrackSearchException()
        }
    }
}