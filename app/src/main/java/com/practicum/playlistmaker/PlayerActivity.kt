package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import retrofit2.Call
import retrofit2.Response

class PlayerActivity : AppCompatActivity() {

    private val itunesService = ApiForItunes.retrofit.create(ApiForItunes::class.java)
    private var playerState = STATE_DEFAULT
    private val playButton: ImageView by lazy { findViewById(R.id.play_button) }
    private val mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val trackNameTextView = findViewById<TextView>(R.id.track_name)
        val musicianNameTextView = findViewById<TextView>(R.id.artist_name)
        val trackTimeTextView = findViewById<TextView>(R.id.track_time)
        val trackDurationTextView = findViewById<TextView>(R.id.track_duration_value)
        val albumTextView = findViewById<TextView>(R.id.album_value)
        val trackReleaseYearTextView = findViewById<TextView>(R.id.track_release_year_value)
        val trackGenreTextView = findViewById<TextView>(R.id.track_genre_value)
        val trackCountryTextView = findViewById<TextView>(R.id.track_country_value)

        val trackId = intent.getStringExtra("track_id") ?: ""

        val searchTrackCallBack = object : retrofit2.Callback<ItunesResponse> {

            override fun onResponse(
                call: Call<ItunesResponse>,
                response: Response<ItunesResponse>
            ) {
                if (response.isSuccessful) {

                    val track = response.body()?.results?.first() ?: return
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
                }
            }


            override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {}

        }

        itunesService.search(trackId).enqueue(searchTrackCallBack)

        findViewById<ImageView>(R.id.return_n).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
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
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}