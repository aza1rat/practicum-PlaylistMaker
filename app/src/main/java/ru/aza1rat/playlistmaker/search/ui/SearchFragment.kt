package ru.aza1rat.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentSearchBinding
import ru.aza1rat.playlistmaker.player.ui.PlayerFragment
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.adapter.TrackAdapter
import ru.aza1rat.playlistmaker.search.ui.mapper.TrackMapper
import ru.aza1rat.playlistmaker.search.ui.model.SearchState
import ru.aza1rat.playlistmaker.search.ui.view_model.SearchViewModel
import ru.aza1rat.playlistmaker.util.ui.getNavController
import kotlin.getValue

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModel<SearchViewModel>()
    private var textWatcher: TextWatcher? = null
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTrackAdapters()
        setAdaptersToRecyclerView()
        attachObservers()
        binding.search.setText(searchViewModel.searchValue)
        setupListeners()
    }

    override fun onDestroyView() {
        binding.tracks.adapter = null
        binding.historyTracks.adapter = null
        textWatcher?.let { binding.search.removeTextChangedListener(it) }
        _binding = null
        super.onDestroyView()
    }

    private fun initTrackAdapters() {
        searchHistoryAdapter = TrackAdapter(
            createDebouncedTrackClickListener { track ->
                navigateToPlayer(
                    requireActivity().getNavController(R.id.fragmentContainer),
                    track
                )
            })
        searchViewModel.observeSearchHistoryTracks().observe(viewLifecycleOwner) {
            searchHistoryAdapter.trackList = it
        }
        trackAdapter = TrackAdapter(
            createDebouncedTrackClickListener { track ->
                searchViewModel.addTrackToSearchHistory(track)
                navigateToPlayer(
                    requireActivity().getNavController(R.id.fragmentContainer),
                    track
                )
            })
    }

    private fun navigateToPlayer(navController: NavController, track: Track) {
        navController.navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(TrackMapper.mapToTrackParcelable(track))
        )
    }

    private fun createDebouncedTrackClickListener(onTrackClickListener: TrackAdapter.OnTrackClickListener): TrackAdapter.OnTrackClickListener {
        return TrackAdapter.OnTrackClickListener { track ->
            if (searchViewModel.clickOnTrackAllowed) {
                searchViewModel.trackClickDebounce()
                onTrackClickListener.onTrackClick(track)
            }
        }
    }

    private fun setAdaptersToRecyclerView() {
        binding.historyTracks.adapter = searchHistoryAdapter
        binding.tracks.adapter = trackAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun attachObservers() {
        searchViewModel.observeSearchHistoryEvent().observe(viewLifecycleOwner) {
            when (it) {
                is SearchViewModel.SearchHistoryEvent.TrackInserted -> {
                    searchHistoryAdapter.notifyItemInserted(it.position)
                    searchHistoryAdapter.notifyItemRangeChanged(
                        it.position, searchHistoryAdapter.trackList.size
                    )
                    binding.historyTracks.layoutManager?.scrollToPosition(0)
                }

                is SearchViewModel.SearchHistoryEvent.TrackRemoved -> {
                    searchHistoryAdapter.notifyItemRemoved(it.position)
                }

                is SearchViewModel.SearchHistoryEvent.TracksCleared -> {
                    searchHistoryAdapter.notifyItemRangeRemoved(0, it.count)
                }
            }
        }

        searchViewModel.observeSearchState().observe(viewLifecycleOwner) {
            when (it) {
                is SearchState.Error -> showMainView(binding.noInternet)
                is SearchState.Empty -> showMainView(binding.searchEmpty)
                is SearchState.Loading -> showMainView(binding.requestProgress)
                is SearchState.TracksContent -> {
                    trackAdapter.trackList = it.tracks
                    trackAdapter.notifyDataSetChanged()
                    showMainView(binding.tracks)
                }

                is SearchState.SearchHistory -> showMainView(binding.searchHistory)
                is SearchState.Idle -> showMainView(null)
            }
        }
    }

    private fun setupListeners() {
        binding.historyClear.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }
        binding.refresh.setOnClickListener {
            searchViewModel.doSearch(binding.search.text.toString())
        }

        binding.search.onFocusChangeListener = createOnFocusChangeListener()

        binding.clearSearch.setOnClickListener {
            binding.search.text.clear()
            hideKeyboard(binding.search)
            showMainView(null)
        }
        textWatcher = createTextWatcher()
        textWatcher?.let {
            binding.search.addTextChangedListener(it)
        }
    }

    private fun createOnFocusChangeListener(): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.search.text.isNullOrEmpty()) {
                if (searchHistoryAdapter.trackList.isNotEmpty()) {
                    showMainView(binding.searchHistory)
                }
            } else {
                binding.searchHistory.isVisible = false
            }
        }
    }

    private fun hideKeyboard(view: View) {
        (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            this.hideSoftInputFromWindow(view.windowToken, 0)
        }
        view.clearFocus()
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearch.isVisible = !s.isNullOrEmpty()
                searchViewModel.onTextChanged(
                    s.toString(),
                    binding.search.hasFocus(),
                    binding.searchHistory.isVisible
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
    }

    private fun showMainView(view: View?) {
        binding.tracks.apply { this.isVisible = this == view }
        binding.searchEmpty.apply { this.isVisible = this == view }
        binding.noInternet.apply { this.isVisible = this == view }
        binding.searchHistory.apply { this.isVisible = this == view }
        binding.requestProgress.apply { this.isVisible = this == view }
    }
}