package ru.aza1rat.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentPlayerBinding
import ru.aza1rat.playlistmaker.media_library.ui.adapter.PlaylistTracksCountAdapter
import ru.aza1rat.playlistmaker.player.ui.mapper.TrackUIMapper
import ru.aza1rat.playlistmaker.player.ui.model.PlayerState
import ru.aza1rat.playlistmaker.player.ui.model.PlaylistSheetState
import ru.aza1rat.playlistmaker.player.ui.model.TrackAddedToPlaylistEvent
import ru.aza1rat.playlistmaker.player.ui.view_model.PlayerViewModel
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.model.TrackUI
import ru.aza1rat.playlistmaker.util.ui.getNavController
import kotlin.getValue

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val playerViewModel: PlayerViewModel by viewModel<PlayerViewModel>()
    private var playlistBottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private lateinit var playlistTracksCountAdapter: PlaylistTracksCountAdapter
    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
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
        setupBottomSheet()
        setupPlaylistTracksCountAdapter(track)
        bindData(track)
        attachObservers()
        playerViewModel.preparePlayer(track.previewUrl ?: "", track.trackId)
        setupListeners(track)
    }

    override fun onPause() {
        playerViewModel.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        bottomSheetCallback?.let { playlistBottomSheetBehavior?.removeBottomSheetCallback(it) }
        bottomSheetCallback = null
        binding.playlistsRecycler.adapter = null
        playlistBottomSheetBehavior = null
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

    private fun setupBottomSheet() {
        playlistBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistSheet)
        playlistBottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupPlaylistTracksCountAdapter(track: Track) {
        playlistTracksCountAdapter = PlaylistTracksCountAdapter( PlaylistTracksCountAdapter.ViewType.PLAYLIST_SHEET_ITEM) { playlist ->
            playerViewModel.addTrackToPlaylist(track, playlist)
        }
        binding.playlistsRecycler.adapter = playlistTracksCountAdapter
    }

    private fun attachObservers() {
        playerViewModel.observePlayerState().observe(viewLifecycleOwner) {
            if (it.trackIsFavourite) {
                binding.favorite.setImageResource(R.drawable.ic_favourite_active_25x23)
            }
            else {
                binding.favorite.setImageResource(R.drawable.ic_favorite_25x23)
            }
            binding.play.isEnabled = it !is PlayerState.NotReady
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

        playerViewModel.observePlaylistSheetState().observe(viewLifecycleOwner) {
            when(it) {
                is PlaylistSheetState.Content -> {
                    playlistTracksCountAdapter.playlists = it.playlists
                    playlistTracksCountAdapter.notifyDataSetChanged()
                }
                is PlaylistSheetState.Empty -> {
                    if (playlistTracksCountAdapter.playlists.isNotEmpty()) {
                        val size = playlistTracksCountAdapter.playlists.size
                        playlistTracksCountAdapter.playlists = emptyList()
                        playlistTracksCountAdapter.notifyItemRangeRemoved(0,size)
                    }
                }
            }
        }

        playerViewModel.observeTrackAddedToPlaylistEvent().observe(viewLifecycleOwner) {
            when(it) {
                is TrackAddedToPlaylistEvent.Added -> {
                    playlistBottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(
                        context, context?.getString(R.string.param_track_added_to_playlist, it.playlistName),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is TrackAddedToPlaylistEvent.AlreadyExists -> Toast.makeText(context,context?.getString(R.string.param_track_already_added_to_playlist, it.playlistName),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupListeners(track: Track) {
        binding.play.setOnClickListener {
            playerViewModel.togglePlay()
        }
        binding.favorite.setOnClickListener {
            playerViewModel.toggleFavourite(track)
        }
        binding.back.setOnClickListener {
            requireActivity().getNavController(R.id.fragmentContainer).navigateUp()
        }
        binding.addToPlaylist.setOnClickListener {
            playerViewModel.getPlaylists()
            playlistBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.newPlaylist.setOnClickListener {
            requireActivity().getNavController(R.id.fragmentContainer).navigate(R.id.action_playerFragment_to_createPlaylistFragment)
        }
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.dim.visibility = View.GONE
                } else {
                    binding.dim.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }
        bottomSheetCallback?.let { playlistBottomSheetBehavior?.addBottomSheetCallback(it) }
    }


    companion object {
        private const val INTENT_TRACK_EXTRA_KEY = "TRACK"
        fun createArgs(track: TrackUI): Bundle = bundleOf(INTENT_TRACK_EXTRA_KEY to track)
    }
}