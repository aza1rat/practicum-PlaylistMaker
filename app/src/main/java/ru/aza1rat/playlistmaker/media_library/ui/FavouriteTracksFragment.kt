package ru.aza1rat.playlistmaker.media_library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentFavouriteTracksBinding
import ru.aza1rat.playlistmaker.media_library.ui.model.FavouritesState
import ru.aza1rat.playlistmaker.media_library.ui.view_model.FavouriteTracksViewModel
import ru.aza1rat.playlistmaker.player.ui.PlayerFragment
import ru.aza1rat.playlistmaker.search.ui.adapter.TrackAdapter
import ru.aza1rat.playlistmaker.search.ui.mapper.TrackMapper
import ru.aza1rat.playlistmaker.util.ui.getNavController
import ru.aza1rat.playlistmaker.util.ui.showMainView

class FavouriteTracksFragment: Fragment() {
    private val favouriteTracksViewModel: FavouriteTracksViewModel by viewModel<FavouriteTracksViewModel>()
    private lateinit var favouritesAdapter: TrackAdapter
    private var _binding: FragmentFavouriteTracksBinding? = null
    private val binding get() = _binding!!
    private var _showContent: ((View?) -> Unit)? = null
    private val showContent get() = _showContent!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _showContent = showMainView(binding.favourites,binding.emptyLibrary)
        setupAdapter()
        attachObserver()
        favouriteTracksViewModel.getFavourites()
    }

    override fun onDestroyView() {
        binding.favourites.adapter = null
        _showContent = null
        _binding = null
        super.onDestroyView()
    }

    private fun setupAdapter() {
        favouritesAdapter = TrackAdapter { track ->
            requireActivity().getNavController(R.id.fragmentContainer).navigate(
                R.id.action_mediaLibraryFragment_to_playerFragment,
                PlayerFragment.createArgs(TrackMapper.mapToTrackParcelable(track))
            )
        }
        binding.favourites.adapter = favouritesAdapter
    }
    private fun attachObserver() {
        favouriteTracksViewModel.observeFavouritesState().observe(viewLifecycleOwner) {
            when (it) {
                is FavouritesState.Empty -> {
                    showContent.invoke(binding.emptyLibrary)
                }
                is FavouritesState.FavouritesContent -> {
                    favouritesAdapter.trackList = it.favourites
                    favouritesAdapter.notifyDataSetChanged()
                    showContent.invoke(binding.favourites)
                }
            }
        }
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }
}