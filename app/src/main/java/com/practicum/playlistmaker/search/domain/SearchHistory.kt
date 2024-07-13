package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.Track

interface SearchHistory {

    fun get(): Array<Track>

    fun add(track: Track)

    fun clear()

}