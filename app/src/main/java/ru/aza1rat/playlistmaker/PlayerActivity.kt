package ru.aza1rat.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import ru.aza1rat.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private var playerState = PLAYER_STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private val progressFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val handler = Handler(Looper.getMainLooper())
    private val progressCheckTask = object : Runnable {
        override fun run() {
            if (::progressPlayingTextView.isInitialized && !isFinishing && !isDestroyed) {
                progressPlayingTextView.text = progressFormatter.format(mediaPlayer.currentPosition)
                if (playerState == PLAYER_STATE_PLAYING) {
                    handler.postDelayed(this, PROGRESS_CHECK_DELAY)
                }
            }
        }
    }

    private lateinit var playButton: ImageButton
    private lateinit var progressPlayingTextView: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val track = intent.getParcelableExtra<Track>(INTENT_TRACK_EXTRA_KEY)
        if (track == null) {
            finish()
            return
        }
        progressPlayingTextView = findViewById(R.id.progressPlaying)
        playButton = findViewById(R.id.play)
        playButton.isEnabled = false
        preparePlayer(track)
        playButton.setOnClickListener {
            when (playerState) {
                PLAYER_STATE_PLAYING -> pausePlayer()
                PLAYER_STATE_PREPARED,PLAYER_STATE_PAUSED -> startPlayer()
            }
        }
        val trackNameTextView = findViewById<MaterialTextView>(R.id.trackName)
        trackNameTextView.text = track.trackName
         val trackArtistTextView = findViewById<MaterialTextView>(R.id.trackArtist)
         trackArtistTextView.text = track.artistName
        val durationTextView = findViewById<MaterialTextView>(R.id.duration)
        durationTextView.text = track.trackTime
        val albumGroup = findViewById<Group>(R.id.albumGroup)
        val albumTextView = findViewById<MaterialTextView>(R.id.album)
        if (!hideGroupWhenNullValue(albumGroup,track.collectionName))
            albumTextView.text = track.collectionName
        val yearGroup = findViewById<Group>(R.id.yearGroup)
        val yearTextView = findViewById<MaterialTextView>(R.id.year)
        val releaseYear = track.releaseYear
        if (!hideGroupWhenNullValue(yearGroup,releaseYear))
            yearTextView.text = releaseYear
        val genreTextView = findViewById<MaterialTextView>(R.id.genre)
        genreTextView.text = track.primaryGenreName
        val countryTextView = findViewById<MaterialTextView>(R.id.country)
        countryTextView.text = track.country
        val coverImageView = findViewById<ImageView>(R.id.cover)
        Glide.with(this).load(track.getArtworkUrl512())
            .placeholder(R.drawable.ph_track_45)
            .into(coverImageView)
        val backImageButton = findViewById<ImageButton>(R.id.back)
        backImageButton.setOnClickListener { finish() }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    private fun hideGroupWhenNullValue(group: Group,value: String?): Boolean {
        group.isVisible = value != null
        return !group.isVisible
    }


    private fun preparePlayer(track: Track) {
        mediaPlayer.setOnPreparedListener {
            playerState = PLAYER_STATE_PREPARED
            playButton.isEnabled = true
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PLAYER_STATE_PREPARED
            playButton.setImageResource(R.drawable.ic_play_84)
            handler.removeCallbacks(progressCheckTask)
            progressPlayingTextView.text = getString(R.string.placeholder_progress_playing)
        }
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = PLAYER_STATE_PLAYING
        playButton.setImageResource(R.drawable.ic_pause_84)
        handler.postDelayed(progressCheckTask, PROGRESS_CHECK_DELAY)
    }

    private fun pausePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            playerState = PLAYER_STATE_PAUSED
            playButton.setImageResource(R.drawable.ic_play_84)
            handler.removeCallbacks(progressCheckTask)
        }
    }

    companion object {
        const val INTENT_TRACK_EXTRA_KEY = "TRACK"
        private const val PROGRESS_CHECK_DELAY = 400L
        private const val PLAYER_STATE_DEFAULT = 0
        private const val PLAYER_STATE_PREPARED = 1
        private const val PLAYER_STATE_PLAYING = 2
        private const val PLAYER_STATE_PAUSED = 3
    }
}