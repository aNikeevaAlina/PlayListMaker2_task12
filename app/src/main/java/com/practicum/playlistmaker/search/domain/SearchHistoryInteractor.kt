package com.practicum.playlistmaker.search.domain

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.data.SearchHistoryImpl

class SearchHistoryInteractor(sharedPreferences: SharedPreferences) {

    private val searchHistory: SearchHistory = SearchHistoryImpl(sharedPreferences)

     fun get(): Array<Track>{
        return searchHistory.get()
    }

     fun add(track: Track) {
        searchHistory.add(track)
    }
}