package com.practicum.playlistmaker.search.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiForItunes {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ItunesResponse>

    companion object {
        private const val BASE_URL = "https://itunes.apple.com"

         val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}