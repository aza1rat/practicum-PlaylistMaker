package ru.aza1rat.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {
    private var searchValue: String = ""
    private lateinit var searchTextEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val clearImageView = findViewById<ImageView>(R.id.clear)
        searchTextEdit = findViewById(R.id.search)
        clearImageView.setOnClickListener {
            searchTextEdit.text.clear()
            hideKeyboard(searchTextEdit)
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
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            this.hideSoftInputFromWindow(view.windowToken, 0)
        }
        view.clearFocus()
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}