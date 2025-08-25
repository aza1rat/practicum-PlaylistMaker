package ru.aza1rat.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
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
    private lateinit var tracksRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val clearImageView = findViewById<ImageView>(R.id.clear)
        searchTextEdit = findViewById(R.id.search)

        clearImageView.setOnClickListener {
            searchTextEdit.text.clear()
            hideKeyboard(searchTextEdit)
            showMainView(null)
        }
        searchTextEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                searchValue = s?.toString() ?: ""
                clearImageView.isVisible = !s.isNullOrEmpty()
            }
        })
        searchEmptyLayout = findViewById(R.id.search_empty)
        noInternetLayout = findViewById(R.id.no_internet)
        tracksRecycler = findViewById(R.id.tracks)
        val trackAdapter = TrackAdapter()
        tracksRecycler.adapter = trackAdapter

        searchTextEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRequest(searchValue, trackAdapter)
                true
            }
            false
        }
        val refreshButton = findViewById<MaterialButton>(R.id.refresh)
        refreshButton.setOnClickListener {
            searchRequest(searchValue, trackAdapter)
        }
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

    private fun showMainView(view: View?) {
        tracksRecycler.apply { this.isVisible = this == view }
        searchEmptyLayout.apply { this.isVisible = this == view }
        noInternetLayout.apply { this.isVisible = this == view }
    }

    private fun searchRequest(songName: String, trackAdapter: TrackAdapter) {
        Client.searchService.getSongs(songName).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TrackResponse>, response: Response<TrackResponse>
            ) {
                if (response.isSuccessful) {
                    val results = response.body()?.results
                    if (results.isNullOrEmpty())
                        showMainView(searchEmptyLayout)
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