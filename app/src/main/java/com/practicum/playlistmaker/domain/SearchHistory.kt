package com.practicum.playlistmaker.domain

interface SearchHistory {

    fun get(): Array<Track>

    fun add(track: Track)

}