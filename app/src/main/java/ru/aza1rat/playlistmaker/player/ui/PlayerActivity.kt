package ru.aza1rat.playlistmaker.player.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.ActivityPlayerBinding
import ru.aza1rat.playlistmaker.player.ui.mapper.TrackUIMapper
import ru.aza1rat.playlistmaker.player.ui.model.PlayerState
import ru.aza1rat.playlistmaker.player.ui.view_model.PlayerViewModel
import ru.aza1rat.playlistmaker.search.ui.model.TrackUI

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val playerViewModel: PlayerViewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val trackUI = intent.getParcelableExtra<TrackUI>(INTENT_TRACK_EXTRA_KEY) ?: run {finish(); return@onCreate}
        val track = TrackUIMapper.mapToTrack(trackUI)
        binding.play.isEnabled = false
        attachObservers()
        binding.play.setOnClickListener {
            playerViewModel.playOrPause()
        }
        playerViewModel.preparePlayer(track.previewUrl ?: "")

        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.duration.text = track.trackTime
        if (!hideGroupWhenNullValue(binding.albumGroup,track.collectionName))
            binding.album.text = track.collectionName
        val releaseYear = track.releaseYear
        if (!hideGroupWhenNullValue(binding.yearGroup,releaseYear))
            binding.year.text = releaseYear
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country
        Glide.with(this).load(track.getArtworkUrl512())
            .placeholder(R.drawable.ph_track_45)
            .into(binding.cover)
        binding.back.setOnClickListener { finish() }
    }

    override fun onPause() {
        playerViewModel.pause()
        super.onPause()
    }

    private fun hideGroupWhenNullValue(group: Group, value: String?): Boolean {
        group.isVisible = value != null
        return !group.isVisible
    }

    private fun attachObservers() {
        playerViewModel.observePlayerState().observe(this) {
            binding.play.isEnabled = it != PlayerState.NotReady
            when (it) {
                is PlayerState.Completed -> {
                    binding.play.setImageResource(R.drawable.ic_play_84)
                    binding.progressPlaying.text = getString(R.string.placeholder_progress_playing)
                }

                is PlayerState.Paused -> {
                    binding.play.setImageResource(R.drawable.ic_play_84)
                    binding.progressPlaying.text = it.progress
                }
                is PlayerState.Playing -> {
                    binding.play.setImageResource(R.drawable.ic_pause_84)
                    binding.progressPlaying.text = it.progress
                }
                else -> {}
            }
        }
    }

    companion object {
        const val INTENT_TRACK_EXTRA_KEY = "TRACK"
    }
}