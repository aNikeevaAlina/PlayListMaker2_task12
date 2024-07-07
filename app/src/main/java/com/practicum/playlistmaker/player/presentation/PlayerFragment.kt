package com.practicum.playlistmaker.player.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.player.presentation.model.PlayerScreenState
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker2.R
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment(R.layout.fragment_player) {
    
    private var playerState = STATE_DEFAULT
    private lateinit var playButton: ImageView
    private lateinit var trackTimeTextView : TextView
    private val mediaPlayer = MediaPlayer()
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playButton = view. findViewById(R.id.play_button)
        trackTimeTextView = view.findViewById(R.id.track_time)
        val trackNameTextView = view.findViewById<TextView>(R.id.track_name)
        val musicianNameTextView = view.findViewById<TextView>(R.id.artist_name)
        val trackDurationTextView = view.findViewById<TextView>(R.id.track_duration_value)
        val albumTextView = view.findViewById<TextView>(R.id.album_value)
        val trackReleaseYearTextView = view.findViewById<TextView>(R.id.track_release_year_value)
        val trackGenreTextView = view.findViewById<TextView>(R.id.track_genre_value)
        val trackCountryTextView = view.findViewById<TextView>(R.id.track_country_value)

        val track = arguments?.let { BundleCompat.getParcelable(it, "track", Track::class.java) } ?: return

        trackNameTextView.text = track.trackName
        musicianNameTextView.text = track.artistName
        trackDurationTextView.text = track.timeFormat()
        albumTextView.text = track.collectionName
        trackReleaseYearTextView.text = track.releaseDate.take(4)
        trackGenreTextView.text = track.primaryGenreName
        trackCountryTextView.text = track.country
        preparePlayer(track.previewUrl)
        playButton.setOnClickListener {
            playbackControl()
        }

        Glide.with(this@PlayerFragment)
            .load(track.getCoverArtwork())
            .transform(
                CenterInside(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_recycler_element))
            )
            .placeholder(R.drawable.ic_vector)
            .into(view.findViewById(R.id.track_poster))


        view.findViewById<ImageView>(R.id.return_n).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerStateFlow.collect {
                    when (it) {
                        is PlayerScreenState.TrackTime -> trackTimeTextView.text = it.currentTime
                    }
                }
            }
        }
    }
    
    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
            playerState = STATE_PREPARED
            viewModel.stopCount()
            viewModel.resetTrackTime()
        }
    }
    
    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause_button)
        viewModel.startCount(mediaPlayer)
        playerState = STATE_PLAYING
    }
    
    private fun pausePlayer() {
        mediaPlayer.pause()
        viewModel.stopCount()
        playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
        playerState = STATE_PAUSED
    }
    
    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopCount()
        mediaPlayer.release()
    }
    
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}