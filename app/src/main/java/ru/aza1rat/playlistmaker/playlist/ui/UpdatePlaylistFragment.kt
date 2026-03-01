package ru.aza1rat.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist
import ru.aza1rat.playlistmaker.playlist.ui.api.BaseInputPlaylistFragment
import ru.aza1rat.playlistmaker.playlist.ui.mapper.PlaylistMapper
import ru.aza1rat.playlistmaker.playlist.ui.model.PlaylistUI
import ru.aza1rat.playlistmaker.playlist.ui.view_model.UpdatePlaylistViewModel

class UpdatePlaylistFragment : BaseInputPlaylistFragment<UpdatePlaylistViewModel>() {
    override val playlistViewModel: UpdatePlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeaderAndMainButtonText()
        val playlistUI = arguments?.getParcelable<PlaylistUI>(PLAYLIST_EXTRA_KEY) ?: run {
            navigateUp(); return@onViewCreated
        }
        val playlist = PlaylistMapper.map(playlistUI)
        bindPlaylist(playlist)
        playlistViewModel.setCurrentPlaylist(playlist)
        binding.createPlaylist.setOnClickListener {
            playlistViewModel.updatePlaylist(
                binding.name.text.toString(),
                binding.description.text.toString()
            )
        }
    }

    private fun setHeaderAndMainButtonText() {
        binding.header.text = getString(R.string.update_playlist)
        binding.createPlaylist.text = getString(R.string.save)
    }

    private fun bindPlaylist(playlist: Playlist) {
        binding.name.setText(playlist.name)
        binding.description.setText(playlist.description)
        Glide.with(this)
            .load(playlist.coverUri)
            .placeholder(R.drawable.ph_track_45)
            .into(binding.coverPlaylist)
    }

    override fun onPerformExit(): Boolean {
        return true
    }

    companion object {
        private const val PLAYLIST_EXTRA_KEY = "PLAYLIST"
        fun createArgs(playlist: PlaylistUI): Bundle = bundleOf(PLAYLIST_EXTRA_KEY to playlist)
    }
}