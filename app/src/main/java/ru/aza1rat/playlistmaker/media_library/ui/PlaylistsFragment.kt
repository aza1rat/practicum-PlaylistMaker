package ru.aza1rat.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.aza1rat.playlistmaker.media_library.ui.adapter.PlaylistTracksCountAdapter
import ru.aza1rat.playlistmaker.media_library.ui.model.PlaylistState
import ru.aza1rat.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import ru.aza1rat.playlistmaker.util.ui.getNavController
import ru.aza1rat.playlistmaker.util.ui.showMainView

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private var _showContent: ((View?) -> Unit)? = null
    private val showContent get() = _showContent!!
    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _showContent = showMainView(binding.emptyPlaylists, binding.playlists)
        binding.newPlaylist.setOnClickListener {
            requireActivity().getNavController(R.id.fragmentContainer).navigate(
                R.id.action_mediaLibraryFragment_to_createPlaylistFragment
            )
        }
        val adapter = PlaylistTracksCountAdapter(PlaylistTracksCountAdapter.ViewType.PLAYLIST_GRID_ITEM)
        binding.playlists.adapter = adapter
        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.Content -> {
                    adapter.playlists = it.playlists
                    adapter.notifyDataSetChanged()
                    showContent.invoke(binding.playlists)
                }
                is PlaylistState.Empty -> {
                    showContent.invoke(binding.emptyPlaylists)
                }
            }
        }
        viewModel.getPlaylists()
    }

    override fun onDestroyView() {
        binding.playlists.adapter = null
        _showContent = null
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}