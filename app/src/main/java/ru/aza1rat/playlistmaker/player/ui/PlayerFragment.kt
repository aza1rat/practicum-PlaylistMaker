package ru.aza1rat.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentPlayerBinding
import ru.aza1rat.playlistmaker.player.ui.mapper.TrackUIMapper
import ru.aza1rat.playlistmaker.player.ui.model.PlayerState
import ru.aza1rat.playlistmaker.player.ui.view_model.PlayerViewModel
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.model.TrackUI
import ru.aza1rat.playlistmaker.util.ui.getNavController
import kotlin.getValue

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val playerViewModel: PlayerViewModel by viewModel<PlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.play.isEnabled = false
        val trackUI = arguments?.getParcelable<TrackUI>(INTENT_TRACK_EXTRA_KEY) ?: run {
            requireActivity().getNavController(R.id.fragmentContainer)
                .navigateUp(); return@onViewCreated
        }
        val track = TrackUIMapper.mapToTrack(trackUI)
        bindData(track)
        attachObservers()
        playerViewModel.preparePlayer(track.previewUrl ?: "")
        setupListeners()
    }

    override fun onPause() {
        playerViewModel.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun bindData(track: Track) {
        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.duration.text = track.trackTime
        if (!hideGroupWhenNullValue(binding.albumGroup, track.collectionName)) binding.album.text =
            track.collectionName
        val releaseYear = track.releaseYear
        if (!hideGroupWhenNullValue(binding.yearGroup, releaseYear)) binding.year.text = releaseYear
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country
        Glide.with(this).load(track.getArtworkUrl512()).placeholder(R.drawable.ph_track_45)
            .into(binding.cover)
    }

    private fun hideGroupWhenNullValue(group: Group, value: String?): Boolean {
        group.isVisible = value != null
        return !group.isVisible
    }

    private fun attachObservers() {
        playerViewModel.observePlayerState().observe(viewLifecycleOwner) {
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

    private fun setupListeners() {
        binding.play.setOnClickListener {
            playerViewModel.playOrPause()
        }
        binding.back.setOnClickListener {
            requireActivity().getNavController(R.id.fragmentContainer).navigateUp()
        }
    }


    companion object {
        private const val INTENT_TRACK_EXTRA_KEY = "TRACK"
        fun createArgs(track: TrackUI): Bundle = bundleOf(INTENT_TRACK_EXTRA_KEY to track)
    }
}