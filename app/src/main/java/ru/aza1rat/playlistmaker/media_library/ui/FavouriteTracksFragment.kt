package ru.aza1rat.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.databinding.FragmentFavouriteTracksBinding
import ru.aza1rat.playlistmaker.media_library.ui.view_model.FavouriteTracksViewModel

class FavouriteTracksFragment: Fragment() {
    private var _binding: FragmentFavouriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavouriteTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }
}