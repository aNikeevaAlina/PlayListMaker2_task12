package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Int,
    val trackId: String,
    val country: String,
    val primaryGenreName: String,
    val collectionName: String,
    val releaseDate: String,
    val previewUrl: String
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun timeFormat(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}


