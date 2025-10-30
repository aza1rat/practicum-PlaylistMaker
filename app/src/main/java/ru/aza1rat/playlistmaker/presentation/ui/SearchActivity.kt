package ru.aza1rat.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ru.aza1rat.playlistmaker.Creator
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.domain.api.interactor.SearchHistoryInteractor
import ru.aza1rat.playlistmaker.domain.api.interactor.TrackInteractor
import ru.aza1rat.playlistmaker.domain.api.repository.SearchHistoryRepository
import ru.aza1rat.playlistmaker.domain.model.Track
import ru.aza1rat.playlistmaker.domain.model.TrackSearchResult
import ru.aza1rat.playlistmaker.presentation.adapter.TrackAdapter
import ru.aza1rat.playlistmaker.presentation.mapper.TrackMapper

class SearchActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val sendRequestTask = Runnable {
        if (!isFinishing && !isDestroyed) {
            if (searchValue.isNotEmpty() && searchValue.isNotBlank() && trackAdapter != null) {
                searchRequest(searchValue, trackAdapter!!)
            }
        }
    }
    private var searchValue: String = ""
    private var trackAdapter: TrackAdapter? = null
    private lateinit var searchTextEdit: EditText
    private lateinit var searchEmptyLayout: FrameLayout
    private lateinit var noInternetLayout: FrameLayout
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var requestProgressLayout: FrameLayout
    private lateinit var tracksRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchEmptyLayout = findViewById(R.id.searchEmpty)
        noInternetLayout = findViewById(R.id.noInternet)
        searchHistoryLayout = findViewById(R.id.searchHistory)
        requestProgressLayout = findViewById(R.id.requestProgress)

        val historyTracksRecycler = findViewById<RecyclerView>(R.id.historyTracks)
        val searchHistoryAdapter = TrackAdapter(
            createDebouncedTrackClickListener(historyTracksRecycler) { track ->
                startActivity(createPlayerActivityIntent(track))
            })

        val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
        searchHistoryAdapter.trackList = searchHistoryInteractor.get()

        historyTracksRecycler.adapter = searchHistoryAdapter

        val historyClearButton = findViewById<MaterialButton>(R.id.historyClear)
        historyClearButton.setOnClickListener {
            val removedTracks = searchHistoryInteractor.clear()
            searchHistoryAdapter.notifyItemRangeRemoved(0, removedTracks)
            showMainView(null)
        }
        tracksRecycler = findViewById(R.id.tracks)
        trackAdapter = TrackAdapter(
            createDebouncedTrackClickListener(tracksRecycler) { track ->
                searchHistoryInteractor.add(track,object: SearchHistoryRepository.SearchHistoryCallback {
                    override fun onTrackInserted(position: Int) {
                        searchHistoryAdapter.notifyItemInserted(position)
                        searchHistoryAdapter.notifyItemRangeChanged(
                            0,
                            searchHistoryAdapter.trackList.size - 1
                        )
                    }
                    override fun onTrackRemoved(position: Int) {
                        searchHistoryAdapter.notifyItemRemoved(position)
                    }
                })

                historyTracksRecycler.layoutManager?.scrollToPosition(0)
                startActivity(createPlayerActivityIntent(track))
            })
        tracksRecycler.adapter = trackAdapter
        val refreshButton = findViewById<MaterialButton>(R.id.refresh)
        refreshButton.setOnClickListener {
            searchRequest(searchValue, trackAdapter!!)
        }

        searchTextEdit = findViewById(R.id.search)
        searchTextEdit.onFocusChangeListener = createOnFocusChangeListener(searchHistoryInteractor)

        val clearSearchImageView = findViewById<ImageView>(R.id.clearSearch)
        clearSearchImageView.setOnClickListener {
            searchTextEdit.text.clear()
            hideKeyboard(searchTextEdit)
            showMainView(null)
        }
        searchTextEdit.addTextChangedListener(
            createTextWatcher(
                searchHistoryInteractor, clearSearchImageView
            )
        )
        val backImageButton = findViewById<ImageButton>(R.id.back)
        backImageButton.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        handler.removeCallbacks(sendRequestTask)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(SEARCH_TEXT, "").apply {
            searchValue = this
            searchTextEdit.setText(this)
        }
    }

    private fun hideKeyboard(view: View) {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            this.hideSoftInputFromWindow(view.windowToken, 0)
        }
        view.clearFocus()
    }

    private fun createTextWatcher(
        searchHistory: SearchHistoryInteractor, clearSearchImageView: ImageView
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchValue = s?.toString() ?: ""
                clearSearchImageView.isVisible = !s.isNullOrEmpty()
                if (s.isNullOrEmpty() && searchTextEdit.hasFocus() && searchHistory.get()
                        .isNotEmpty()
                ) {
                    showMainView(searchHistoryLayout)
                } else searchHistoryLayout.isVisible = false
                handler.removeCallbacks(sendRequestTask)
                handler.postDelayed(sendRequestTask, REQUEST_DELAY)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
    }

    private fun createOnFocusChangeListener(searchHistory: SearchHistoryInteractor): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { view, hasFocus ->
            val searchTextEdit: EditText = view as EditText
            if (hasFocus && searchTextEdit.text.isNullOrEmpty() && searchHistory.get()
                    .isNotEmpty()
            ) {
                showMainView(searchHistoryLayout)
            } else searchHistoryLayout.isVisible = false
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
        val trackParcelable = TrackMapper.mapToTrackParcelable(track)
        return Intent(this@SearchActivity, PlayerActivity::class.java).apply {
            this.putExtra(PlayerActivity.Companion.INTENT_TRACK_EXTRA_KEY, trackParcelable)
        }
    }

    private fun showMainView(view: View?) {
        tracksRecycler.apply { this.isVisible = this == view }
        searchEmptyLayout.apply { this.isVisible = this == view }
        noInternetLayout.apply { this.isVisible = this == view }
        searchHistoryLayout.apply { this.isVisible = this == view }
        requestProgressLayout.apply { this.isVisible = this == view }
    }

    private fun searchRequest(songName: String, trackAdapter: TrackAdapter) {
        val trackInteractor = Creator.provideTrackInteractor()
        showMainView(requestProgressLayout)
        trackInteractor.searchTracks(songName, object : TrackInteractor.TrackConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(foundTracks: TrackSearchResult) {
                handler.post {
                    if (foundTracks.result == 200) {
                        if (foundTracks.tracks.isNotEmpty()) {
                            trackAdapter.trackList = foundTracks.tracks
                            trackAdapter.notifyDataSetChanged()
                            showMainView(tracksRecycler)
                        } else
                            showMainView(searchEmptyLayout)
                    } else
                        showMainView(noInternetLayout)
                }
            }
        })
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val REQUEST_DELAY = 2000L
    }
}