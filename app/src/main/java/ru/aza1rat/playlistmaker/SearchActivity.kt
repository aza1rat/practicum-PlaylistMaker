package ru.aza1rat.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.aza1rat.playlistmaker.adapter.TrackAdapter
import ru.aza1rat.playlistmaker.data.Track
import ru.aza1rat.playlistmaker.network.Client
import ru.aza1rat.playlistmaker.network.TrackResponse

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

        val searchHistory = SearchHistory(
            getSharedPreferences(
                App.SHARED_PREFERENCES_NAME, MODE_PRIVATE
            ), searchHistoryAdapter
        )
        searchHistoryAdapter.trackList = searchHistory.getHistory()

        historyTracksRecycler.adapter = searchHistoryAdapter

        val historyClearButton = findViewById<MaterialButton>(R.id.historyClear)
        historyClearButton.setOnClickListener {
            searchHistory.clear()
            showMainView(null)
        }
        tracksRecycler = findViewById(R.id.tracks)
        trackAdapter = TrackAdapter(
            createDebouncedTrackClickListener(tracksRecycler) { track ->
                searchHistory.add(track)
                historyTracksRecycler.layoutManager?.scrollToPosition(0)
                startActivity(createPlayerActivityIntent(track))
            })
        tracksRecycler.adapter = trackAdapter
        val refreshButton = findViewById<MaterialButton>(R.id.refresh)
        refreshButton.setOnClickListener {
            searchRequest(searchValue, trackAdapter!!)
        }

        searchTextEdit = findViewById(R.id.search)
        searchTextEdit.onFocusChangeListener = createOnFocusChangeListener(searchHistory)

        val clearSearchImageView = findViewById<ImageView>(R.id.clearSearch)
        clearSearchImageView.setOnClickListener {
            searchTextEdit.text.clear()
            hideKeyboard(searchTextEdit)
            showMainView(null)
        }
        searchTextEdit.addTextChangedListener(
            createTextWatcher(
                searchHistory, clearSearchImageView
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
        searchHistory: SearchHistory, clearSearchImageView: ImageView
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchValue = s?.toString() ?: ""
                clearSearchImageView.isVisible = !s.isNullOrEmpty()
                if (s.isNullOrEmpty() && searchTextEdit.hasFocus() && searchHistory.getHistory()
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

    private fun createOnFocusChangeListener(searchHistory: SearchHistory): OnFocusChangeListener {
        return OnFocusChangeListener { view, hasFocus ->
            val searchTextEdit: EditText = view as EditText
            if (hasFocus && searchTextEdit.text.isNullOrEmpty() && searchHistory.getHistory()
                    .isNotEmpty()
            ) {
                showMainView(searchHistoryLayout)
            } else searchHistoryLayout.isVisible = false
        }
    }

    private fun createDebouncedTrackClickListener(
        recyclerView: RecyclerView, onTrackClickListener: TrackAdapter.OnTrackClickListener
    ): TrackAdapter.OnTrackClickListener {
        return object : TrackAdapter.OnTrackClickListener {
            override fun onTrackClick(track: Track) {
                recyclerView.isEnabled = false
                onTrackClickListener.onTrackClick(track)
                recyclerView.isEnabled = true
            }
        }
    }

    private fun createPlayerActivityIntent(track: Track): Intent {
        return Intent(this@SearchActivity, PlayerActivity::class.java).apply {
            this.putExtra(PlayerActivity.INTENT_TRACK_EXTRA_KEY, track)
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
        Client.searchService.getSongs(songName).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TrackResponse>, response: Response<TrackResponse>
            ) {
                if (response.isSuccessful) {
                    val results = response.body()?.results
                    if (results.isNullOrEmpty()) showMainView(searchEmptyLayout)
                    else {
                        trackAdapter.trackList = results
                        trackAdapter.notifyDataSetChanged()
                        showMainView(tracksRecycler)
                    }
                } else {
                    showMainView(noInternetLayout)
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                showMainView(noInternetLayout)
            }

        })
        showMainView(requestProgressLayout)
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val REQUEST_DELAY = 2000L
    }
}