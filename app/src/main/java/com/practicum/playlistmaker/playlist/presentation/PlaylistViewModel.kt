package com.practicum.playlistmaker.playlist.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.createplaylist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.presentation.model.DetailedPlaylistModel
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker2.R
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor): ViewModel() {

    var playlistFlow: Flow<DetailedPlaylistModel>? = null

    private val _removingState = MutableStateFlow(false)
    val removingState = _removingState.asStateFlow()

    fun getPlaylistById(id: Int) {
        playlistFlow = interactor.getPlaylistById(id)
    }
    fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {
        viewModelScope.launch {
            interactor.deleteTrackFromPlaylist(trackId, playlistId)
        }
    }
    fun deletePlaylist(playlist: DetailedPlaylistModel) {
        viewModelScope.launch {
            val removingResult = interactor.deletePlaylistById(playlist)
            _removingState.value = removingResult
        }
    }
    fun getShareIntent(trackList: List<Track>, type: String, context: Context): Intent {
        val text =  buildString {
            append(context.resources.getQuantityString(R.plurals.tracks_count, trackList.size, trackList.size))
            append("\n")
            trackList.forEachIndexed { index, track ->
                append("${index + 1}. ${track.artistName} - ${track.trackName} (${track.timeFormat()})")
                append("\n")
            }
        }
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            this.type = type
        }

        return Intent.createChooser(sendIntent, null)
    }
}