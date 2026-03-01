package ru.aza1rat.playlistmaker.playlist.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentPlaylistBinding
import ru.aza1rat.playlistmaker.player.ui.PlayerFragment
import ru.aza1rat.playlistmaker.playlist.domain.model.PlaylistTracks
import ru.aza1rat.playlistmaker.playlist.ui.mapper.PlaylistMapper
import ru.aza1rat.playlistmaker.playlist.ui.model.PlaylistDialogEvent
import ru.aza1rat.playlistmaker.playlist.ui.model.PlaylistState
import ru.aza1rat.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.adapter.TrackAdapter
import ru.aza1rat.playlistmaker.search.ui.mapper.TrackMapper
import ru.aza1rat.playlistmaker.util.ui.getNavController
import ru.aza1rat.playlistmaker.util.ui.getScreenWidth
import ru.aza1rat.playlistmaker.util.ui.showMainView
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter
    private var moreSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var moreSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null
    private var _showContent: ((View?) -> Unit)? = null
    private val showContent get() = _showContent!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _showContent = showMainView(binding.tracksInPlaylist, binding.emptyPlaylist)
        val playlistId = requireArguments().getInt(PLAYLIST_ID_KEY)
        setupMoreSheet()
        setupAdapter()
        setupListeners()
        attachObservers()
        viewModel.getPlaylistTracks(playlistId)
    }

    override fun onDestroyView() {
        moreSheetCallback?.let { moreSheetBehavior?.removeBottomSheetCallback(it) }
        moreSheetCallback = null
        moreSheetBehavior = null
        binding.tracksInPlaylist.adapter = null
        _showContent = null
        _binding = null
        super.onDestroyView()
    }

    private fun setupMoreSheet() {
        moreSheetBehavior = BottomSheetBehavior.from(binding.moreSheet)
        moreSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupAdapter() {
        trackAdapter = TrackAdapter(onTrackClickListener = { track ->
            requireActivity().getNavController(R.id.fragmentContainer).navigate(
                R.id.action_playlistFragment_to_playerFragment,
                PlayerFragment.createArgs(TrackMapper.mapToTrackParcelable(track))
            )
        }, onTrackLongClickListener = { track ->
            showOnDeleteTrackDialog(track.trackId)
        })
        binding.tracksInPlaylist.adapter = trackAdapter
    }

    private fun setupListeners() {
        binding.back.setOnClickListener {
            navigateUp()
        }
        binding.moreDeletePlaylist.setOnClickListener {
            moreSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.onDeletePlaylistClick()
        }
        binding.moreShare.setOnClickListener {
            moreSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.onShareClick()
        }
        binding.share.setOnClickListener { viewModel.onShareClick() }
        binding.more.setOnClickListener {
            moreSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        moreSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.dim.visibility = View.GONE
                } else {
                    binding.dim.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }
        moreSheetCallback?.let { moreSheetBehavior?.addBottomSheetCallback(it) }
    }

    private fun attachObservers() {
        viewModel.observePlaylistState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistState.Content -> bindPlaylistTracks(state.playlistTracks)
                is PlaylistState.TrackRemoved -> {
                    val previousSize = trackAdapter.trackList.size
                    trackAdapter.trackList = state.playlistTracks.tracks
                    updateTrackSummaryInfo(trackAdapter.trackList)
                    setTrackListVisibility(trackAdapter.trackList)
                    if (previousSize > state.playlistTracks.tracks.size) {
                        trackAdapter.notifyItemRemoved(state.trackIndex)
                        trackAdapter.notifyItemRangeChanged(
                            state.trackIndex,
                            trackAdapter.trackList.size
                        )
                    }
                }

                is PlaylistState.Deleted -> navigateUp()
            }
        }
        viewModel.observePlaylistDialogEvent().observe(viewLifecycleOwner) { event ->
            when (event) {
                is PlaylistDialogEvent.UnableToShare -> Toast.makeText(
                    requireActivity(), R.string.share_playlist_empty_tracks, Toast.LENGTH_SHORT
                ).show()

                is PlaylistDialogEvent.ShareTracksMessage -> {
                    val tracksCountMessage = resources.getQuantityString(
                        R.plurals.plural_track_count, event.tracksCount, event.tracksCount
                    )
                    val message = event.message.invoke(tracksCountMessage)
                    viewModel.sharePlaylist(message)
                }

                is PlaylistDialogEvent.DeletePlaylistDialog -> {
                    showOnDeletePlaylist()
                }
            }
        }
    }

    private fun bindPlaylistTracks(playlistTracks: PlaylistTracks) {
        binding.name.text = playlistTracks.playlist.name
        binding.morePlaylistName.text = playlistTracks.playlist.name
        if (playlistTracks.playlist.description != null) {
            binding.description.text = playlistTracks.playlist.description
            binding.description.isVisible = true
        } else {
            binding.description.isVisible = false
        }
        updateTrackSummaryInfo(playlistTracks.tracks)
        if (setTrackListVisibility(playlistTracks.tracks)) {
            trackAdapter.trackList = playlistTracks.tracks
            trackAdapter.notifyDataSetChanged()
        }
        setCover(playlistTracks.playlist.coverUri)
        Glide.with(this).load(playlistTracks.playlist.coverUri)
            .placeholder(R.drawable.ph_track_45)
            .into(binding.morePlaylistCover)
        binding.moreEditPlaylist.setOnClickListener {
            requireActivity().getNavController(R.id.fragmentContainer).navigate(
                R.id.action_playlistFragment_to_updatePlaylistFragment,
                UpdatePlaylistFragment.createArgs(PlaylistMapper.map(playlistTracks.playlist))
            )
        }
    }

    private fun updateTrackSummaryInfo(tracks: List<Track>) {
        val playlistDuration = calculatePlaylistDuration(tracks)
        binding.tracksCount.text =
            resources.getQuantityString(R.plurals.plural_track_count, tracks.size, tracks.size)
        binding.tracksMinutes.text = resources.getQuantityString(
            R.plurals.plural_minute_count,
            playlistDuration.first,
            playlistDuration.second
        )
        binding.morePlaylistTracksCount.text =
            resources.getQuantityString(R.plurals.plural_track_count, tracks.size, tracks.size)
    }

    private fun setTrackListVisibility(tracks: List<Track>): Boolean {
        if (tracks.isEmpty()) {
            showContent.invoke(binding.emptyPlaylist)
            return false
        } else {
            showContent.invoke(binding.tracksInPlaylist)
            return true
        }
    }

    private fun setCover(coverUri: Uri?) {
        var coverSize = requireActivity().getScreenWidth()
        coverSize -= binding.back.width * COVER_SHRINK_DUE_BACK_ARROW
        Glide.with(this).load(coverUri)
            .override(coverSize, coverSize)
            .placeholder(R.drawable.ph_track_filled_45)
            .into(binding.cover)
        if(coverUri == null) {
            binding.cover.scaleType = ImageView.ScaleType.CENTER_CROP
        } else
            binding.cover.scaleType = ImageView.ScaleType.CENTER
    }

    private fun calculatePlaylistDuration(tracks: List<Track>): Pair<Int, String> {
        var duration: Long = 0
        tracks.forEach { duration += it.trackTimeMillis }
        val minutesStr = SimpleDateFormat("mm", Locale.getDefault()).format(duration)
        val minutes = minutesStr.toInt()
        return Pair(minutes, minutesStr)
    }

    private fun showOnDeleteTrackDialog(trackId: Int) {
        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.playlist_remove_track_dialog_title))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteTrackFromPlaylist(trackId)
            }.create().show()
    }

    private fun showOnDeletePlaylist() {
        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.playlist_remove_dialog_title))
            .setMessage(getString(R.string.playlist_remove_dialog_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deletePlaylist()
            }.create().show()
    }

    private fun navigateUp() = requireActivity().getNavController(R.id.fragmentContainer).navigateUp()

    companion object {
        private const val PLAYLIST_ID_KEY = "PLAYLIST_ID"
        private const val COVER_SHRINK_DUE_BACK_ARROW = 2
        fun createArgs(playlistId: Int): Bundle = bundleOf(PLAYLIST_ID_KEY to playlistId)
    }
}