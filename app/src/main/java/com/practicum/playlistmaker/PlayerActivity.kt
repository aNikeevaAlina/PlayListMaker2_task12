package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.BundleCompat
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker2.R
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    
    private var playerState = STATE_DEFAULT
    private val playButton: ImageView by lazy { findViewById(R.id.play_button) }
    private val trackTimeTextView by lazy { findViewById<TextView>(R.id.track_time) }
    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var counter = 0L
    private val runnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                trackTimeTextView.text = getTimeString(counter)
                counter += TIME_UPDATE_DELAY
                handler.postDelayed(this, TIME_UPDATE_DELAY)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        
        val trackNameTextView = findViewById<TextView>(R.id.track_name)
        val musicianNameTextView = findViewById<TextView>(R.id.artist_name)
        val trackDurationTextView = findViewById<TextView>(R.id.track_duration_value)
        val albumTextView = findViewById<TextView>(R.id.album_value)
        val trackReleaseYearTextView = findViewById<TextView>(R.id.track_release_year_value)
        val trackGenreTextView = findViewById<TextView>(R.id.track_genre_value)
        val trackCountryTextView = findViewById<TextView>(R.id.track_country_value)
        
        val track = IntentCompat.getParcelableExtra(intent, "track", Track::class.java) ?: return
        
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
        
        Glide.with(this@PlayerActivity)
            .load(track.getCoverArtwork())
            .transform(
                CenterInside(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_recycler_element))
            )
            .placeholder(R.drawable.ic_vector)
            .into(findViewById(R.id.track_poster))
        
        
        findViewById<ImageView>(R.id.return_n).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    
    private fun getTimeString(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(time)
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
            handler.removeCallbacks(runnable)
            counter = 0L
            trackTimeTextView.text = getTimeString(0L)
        }
    }
    
    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause_button)
        handler.post(runnable)
        playerState = STATE_PLAYING
    }
    
    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
        playerState = STATE_PAUSED
        handler.removeCallbacks(runnable)
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
        handler.removeCallbacks(runnable)
        mediaPlayer.release()
    }
    
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_UPDATE_DELAY = 1000L
    }
}