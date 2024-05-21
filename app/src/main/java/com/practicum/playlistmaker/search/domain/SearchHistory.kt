package com.practicum.playlistmaker.search.domain

interface SearchHistory {

    fun get(): Array<Track>

    fun add(track: Track)

    fun clear()

}