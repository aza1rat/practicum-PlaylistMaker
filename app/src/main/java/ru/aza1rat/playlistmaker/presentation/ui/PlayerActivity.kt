package ru.aza1rat.playlistmaker.presentation.ui

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
import ru.aza1rat.playlistmaker.Creator
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.domain.api.interactor.PlayerInteractor
import ru.aza1rat.playlistmaker.domain.api.repository.PlayerRepository.PlayerStateChangeListener
import ru.aza1rat.playlistmaker.domain.model.PlayerState
import ru.aza1rat.playlistmaker.domain.model.Track
import ru.aza1rat.playlistmaker.presentation.mapper.TrackParcelableMapper
import ru.aza1rat.playlistmaker.presentation.model.TrackParcelable
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private val progressFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val handler = Handler(Looper.getMainLooper())
    private val progressCheckTask = object : Runnable {
        override fun run() {
            if (::progressPlayingTextView.isInitialized && !isFinishing && !isDestroyed) {
                progressPlayingTextView.text = progressFormatter.format(playerInteractor.getCurrentPosition())
                if (playerInteractor.getCurrentState() == PlayerState.PLAYING) {
                    handler.postDelayed(this, PROGRESS_CHECK_DELAY)
                }
            }
        }
    }
    private lateinit var playerInteractor: PlayerInteractor

    private lateinit var playButton: ImageButton
    private lateinit var progressPlayingTextView: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val trackParcelable = intent.getParcelableExtra<TrackParcelable>(INTENT_TRACK_EXTRA_KEY)
        if (trackParcelable == null) {
            finish()
            return
        }
        val track = TrackParcelableMapper.mapToTrack(trackParcelable)
        progressPlayingTextView = findViewById(R.id.progressPlaying)
        playButton = findViewById(R.id.play)
        playButton.isEnabled = false
        this.playerInteractor = Creator.providePlayerInteractor()

        preparePlayer(track)
        playButton.setOnClickListener {
            when (playerInteractor.getCurrentState()) {
                PlayerState.PLAYING -> playerInteractor.pausePlayer()
                PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.COMPLETED -> playerInteractor.startPlayer()
                PlayerState.DEFAULT -> {}
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
        playerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        playerInteractor.release()
        super.onDestroy()
    }

    private fun hideGroupWhenNullValue(group: Group, value: String?): Boolean {
        group.isVisible = value != null
        return !group.isVisible
    }


    private fun preparePlayer(track: Track) {
        playerInteractor.setOnPlayerStateChangeListener(object: PlayerStateChangeListener {
            override fun onPlayerStateChanged(state: PlayerState) {
                when (state) {
                    PlayerState.PREPARED -> {
                        playButton.isEnabled = true
                    }
                    PlayerState.COMPLETED -> {
                        playButton.setImageResource(R.drawable.ic_play_84)
                        handler.removeCallbacks(progressCheckTask)
                        progressPlayingTextView.text = getString(R.string.placeholder_progress_playing)
                    }
                    PlayerState.PLAYING -> {
                        playButton.setImageResource(R.drawable.ic_pause_84)
                        handler.postDelayed(progressCheckTask, PROGRESS_CHECK_DELAY)
                    }
                    PlayerState.PAUSED -> {
                        playButton.setImageResource(R.drawable.ic_play_84)
                        handler.removeCallbacks(progressCheckTask)
                    }
                    PlayerState.DEFAULT -> {}
                }
            }
        })
        playerInteractor.preparePlayer(track.previewUrl)
    }

    companion object {
        const val INTENT_TRACK_EXTRA_KEY = "TRACK"
        private const val PROGRESS_CHECK_DELAY = 400L
    }
}