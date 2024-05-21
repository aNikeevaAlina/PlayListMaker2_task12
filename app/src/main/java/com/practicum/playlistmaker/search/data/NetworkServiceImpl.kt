package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.NetworkService
import com.practicum.playlistmaker.search.domain.TrackSearchCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkServiceImpl(private val itunesApi: ApiForItunes) : NetworkService {

    override fun search(text: String, callback: TrackSearchCallback) {
        itunesApi.search(text).enqueue(object : Callback<ItunesResponse> {
            override fun onResponse(
                call: Call<ItunesResponse>,
                response: Response<ItunesResponse>
            ) {
                val result = response.body()
                if (result == null) callback.onError("Something went wrong")
                else callback.onSuccess(result.results)
            }

            override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                callback.onError(t.message ?: "Something went wrong")
            }
        })
    }
}