package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.SearchHistory
import com.practicum.playlistmaker.search.domain.Track

class SearchHistoryImpl (sharedPreferences: SharedPreferences): SearchHistory {

    private val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = sharedPreferences
    }

    override fun get(): Array<Track>{
        val saveJson = sharedPrefs.getString(KEY_LIST_TRACKS, "[ ]")
        return Gson().fromJson(saveJson, Array<Track>::class.java)
    }

    override fun add(track: Track) {
        val listObjects = get().filter { it.trackId != track.trackId }.toMutableList()
        if ( listObjects.size >= 10 ) {
            listObjects.removeLast()
        }
        listObjects.add(0, track)
        val json = Gson().toJson(listObjects)
        sharedPrefs.edit()
            .putString(KEY_LIST_TRACKS, json)
            .apply()
    }

    override fun clear() {
        sharedPrefs.edit().clear().apply()
    }

    companion object {
        const val KEY_LIST_TRACKS = "tracks_history"
    }
}