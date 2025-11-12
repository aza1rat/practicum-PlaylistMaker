package ru.aza1rat.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.aza1rat.playlistmaker.databinding.ActivitySearchBinding
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.player.ui.PlayerActivity
import ru.aza1rat.playlistmaker.search.ui.adapter.TrackAdapter
import ru.aza1rat.playlistmaker.search.ui.mapper.TrackMapper
import ru.aza1rat.playlistmaker.search.ui.model.SearchState
import ru.aza1rat.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.getFactory()
    }
    private var textWatcher: TextWatcher? = null
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchHistoryAdapter = TrackAdapter(
            createDebouncedTrackClickListener(binding.historyTracks) { track ->
                startActivity(createPlayerActivityIntent(track))
            })

        viewModel.observeSearchHistoryTracks().observe(this) {
            searchHistoryAdapter.trackList = it
        }

        binding.historyTracks.adapter = searchHistoryAdapter

        viewModel.observeSearchHistoryEvent().observe(this) {
            when (it) {
                is SearchViewModel.SearchHistoryEvent.TrackInserted -> {
                    searchHistoryAdapter.notifyItemInserted(it.position)
                    searchHistoryAdapter.notifyItemRangeChanged(it.position,
                        searchHistoryAdapter.trackList.size)
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

        viewModel.observeSearchState().observe(this) {
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

        binding.historyClear.setOnClickListener {
            viewModel.clearSearchHistory()
        }
        trackAdapter = TrackAdapter(
            createDebouncedTrackClickListener(binding.tracks) { track ->
                viewModel.addTrackToSearchHistory(track)
                startActivity(createPlayerActivityIntent(track))
            })
        binding.tracks.adapter = trackAdapter
        binding.refresh.setOnClickListener {
            viewModel.doSearch(binding.search.text.toString())
        }
        binding.search.setText(viewModel.searchValue)
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
        binding.back.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.search.removeTextChangedListener(it) }
    }

    private fun hideKeyboard(view: View) {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
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
                viewModel.onTextChanged(s.toString(), binding.search.hasFocus(),
                    binding.searchHistory.isVisible)
            }

            override fun afterTextChanged(s: Editable?) {
            }

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

    private fun createDebouncedTrackClickListener(
        recyclerView: RecyclerView, onTrackClickListener: TrackAdapter.OnTrackClickListener
    ): TrackAdapter.OnTrackClickListener {
        return TrackAdapter.OnTrackClickListener { track ->
            recyclerView.isEnabled = false
            onTrackClickListener.onTrackClick(track)
            recyclerView.isEnabled = true
        }
    }

    private fun createPlayerActivityIntent(track: Track): Intent {
        return Intent(this@SearchActivity, PlayerActivity::class.java).apply {
            this.putExtra(
                PlayerActivity.Companion.INTENT_TRACK_EXTRA_KEY,
                TrackMapper.mapToTrackParcelable(track))
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