package ru.aza1rat.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.aza1rat.playlistmaker.adapter.TrackAdapter
import ru.aza1rat.playlistmaker.network.Client
import ru.aza1rat.playlistmaker.network.TrackResponse

class SearchActivity : AppCompatActivity() {
    private var searchValue: String = ""
    private lateinit var searchTextEdit: EditText
    private lateinit var searchEmptyLayout: FrameLayout
    private lateinit var noInternetLayout: FrameLayout
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var tracksRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchEmptyLayout = findViewById(R.id.searchEmpty)
        noInternetLayout = findViewById(R.id.noInternet)
        searchHistoryLayout = findViewById(R.id.searchHistory)

        val searchHistoryAdapter = TrackAdapter()
        val searchHistory = SearchHistory(
            getSharedPreferences(
                App.SHARED_PREFERENCES_NAME, MODE_PRIVATE
            ), searchHistoryAdapter
        )
        searchHistoryAdapter.trackList = searchHistory.getHistory()

        val historyTracksRecycler = findViewById<RecyclerView>(R.id.historyTracks)
        historyTracksRecycler.adapter = searchHistoryAdapter

        val historyClearButton = findViewById<MaterialButton>(R.id.historyClear)
        historyClearButton.setOnClickListener {
            searchHistory.clear()
            showMainView(null)
        }

        val trackAdapter = TrackAdapter { track ->
            searchHistory.add(track)
            historyTracksRecycler.layoutManager?.scrollToPosition(0)
        }
        tracksRecycler = findViewById<RecyclerView>(R.id.tracks)
        tracksRecycler.adapter = trackAdapter
        val refreshButton = findViewById<MaterialButton>(R.id.refresh)
        refreshButton.setOnClickListener {
            searchRequest(searchValue, trackAdapter)
        }

        searchTextEdit = findViewById<EditText>(R.id.search)
        searchTextEdit.setOnEditorActionListener(createOnEditorActionListener(trackAdapter))
        searchTextEdit.onFocusChangeListener = createOnFocusChangeListener(searchHistory)

        val clearSearchImageView = findViewById<ImageView>(R.id.clearSearch)
        clearSearchImageView.setOnClickListener {
            searchTextEdit.text.clear()
            hideKeyboard(searchTextEdit)
            showMainView(null)
        }
        searchTextEdit.addTextChangedListener(createTextWatcher(searchHistory, clearSearchImageView))
        val backImageView = findViewById<ImageView>(R.id.back)
        backImageView.setOnClickListener { finish() }
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
        searchHistory: SearchHistory,
        clearSearchImageView: ImageView,
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty() && searchTextEdit.hasFocus() && searchHistory.getHistory().isNotEmpty()) {
                    showMainView(searchHistoryLayout)
                } else searchHistoryLayout.isVisible = false
            }

            override fun afterTextChanged(s: Editable?) {
                searchValue = s?.toString() ?: ""
                clearSearchImageView.isVisible = !s.isNullOrEmpty()
            }

        }
    }

    private fun createOnEditorActionListener(trackAdapter: TrackAdapter): OnEditorActionListener {
        return OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRequest(searchValue, trackAdapter)
                true
            }
            false
        }
    }

    private fun createOnFocusChangeListener(searchHistory: SearchHistory): OnFocusChangeListener {
        return OnFocusChangeListener { view, hasFocus ->
            val searchTextEdit: EditText = view as EditText
            if (hasFocus && searchTextEdit.text.isNullOrEmpty() && searchHistory.getHistory().isNotEmpty()) {
                showMainView(searchHistoryLayout)
            } else searchHistoryLayout.isVisible = false
        }
    }

    private fun showMainView(view: View?) {
        tracksRecycler.apply { this.isVisible = this == view }
        searchEmptyLayout.apply { this.isVisible = this == view }
        noInternetLayout.apply { this.isVisible = this == view }
        searchHistoryLayout.apply { this.isVisible = this == view }
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
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}