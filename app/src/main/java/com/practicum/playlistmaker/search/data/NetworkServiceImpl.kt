package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.model.TrackSearchException
import com.practicum.playlistmaker.search.domain.NetworkService
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkServiceImpl(private val itunesApi: ApiForItunes) : NetworkService {

    override fun search(text: String): Flow<List<Track>> {
        return callbackFlow {
            val callback = object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        trySend(body.results)
                    } else {
                        throw TrackSearchException("The data is null")
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    throw TrackSearchException(t.message)
                }
            }
            itunesApi.search(text).enqueue(callback)
            awaitClose { }
        }
    }
}