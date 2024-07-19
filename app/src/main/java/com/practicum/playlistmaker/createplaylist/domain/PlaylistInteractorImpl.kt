package com.practicum.playlistmaker.createplaylist.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.core.net.toUri
import com.practicum.playlistmaker.createplaylist.domain.model.PlaylistModel
import com.practicum.playlistmaker.playlist.presentation.model.DetailedPlaylistModel
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import java.io.File

class PlaylistInteractorImpl(
    private val context: Context,
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun addPlaylist(name: String, description: String?, cover: Uri?) {
        val internalCoverUri = cover?.let { saveToInternal(it, name) }
        playlistRepository.addPlaylist(
            PlaylistModel(
                name = name,
                description = description,
                cover = internalCoverUri?.toString()
            )
        )
    }

    private fun saveToInternal(uri: Uri, fileName: String): Uri {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        val directory = context.getDir("playlist_covers", Context.MODE_PRIVATE)
        val file = File(directory, "$fileName.jpeg")
        val fileOutputStream = file.outputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        return file.toUri()
    }

    override fun getPlaylistsFlow(): Flow<List<PlaylistModel>> {
        return playlistRepository.getPlaylistsFlow()
    }

    override suspend fun getAllPlaylists(): List<PlaylistModel> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
        playlistRepository.addTrackToPlaylist(track, playlistId)
    }

    override fun getPlaylistById(id: Int): Flow<DetailedPlaylistModel> {
        return playlistRepository.getPlaylistById(id)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {
        playlistRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun deletePlaylistById(playlist: DetailedPlaylistModel): Boolean {
        return playlistRepository.deletePlaylistById(playlist)
    }
}