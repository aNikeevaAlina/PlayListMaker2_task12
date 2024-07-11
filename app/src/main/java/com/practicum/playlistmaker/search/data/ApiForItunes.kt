package com.practicum.playlistmaker.search.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiForItunes {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): Response<ItunesResponse>

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}