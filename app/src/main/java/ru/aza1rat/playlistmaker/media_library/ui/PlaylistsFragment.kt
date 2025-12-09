package ru.aza1rat.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.aza1rat.playlistmaker.media_library.ui.view_model.PlaylistsViewModel

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}