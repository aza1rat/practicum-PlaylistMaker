package ru.aza1rat.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.aza1rat.playlistmaker.data.Track
import androidx.core.content.edit
import ru.aza1rat.playlistmaker.adapter.TrackAdapter

class SearchHistory(
    private val sharedPrefs: SharedPreferences, private val adapter: TrackAdapter
) {
    val tracksHistory: ArrayList<Track>
    private val gson = Gson()

    init {
        val searchHistory = sharedPrefs.getString(SEARCH_HISTORY_KEY, "")
        if (searchHistory.isNullOrEmpty()) tracksHistory = arrayListOf()
        else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            tracksHistory = gson.fromJson(searchHistory, type)
        }
    }

    fun add(track: Track) {
        for (i in 0 until tracksHistory.size) {
            if (tracksHistory[i].trackId == track.trackId) {
                tracksHistory.removeAt(i)
                adapter.notifyItemRemoved(i)
                break
            }
        }
        if (tracksHistory.size == HISTORY_MAX_SIZE) {
            tracksHistory.removeAt(HISTORY_MAX_SIZE - 1)
            adapter.notifyItemRemoved(HISTORY_MAX_SIZE - 1)
        }
        tracksHistory.add(0, track)
        adapter.notifyItemInserted(0)
        adapter.notifyItemRangeChanged(0, tracksHistory.size)
        sharedPrefs.edit { putString(SEARCH_HISTORY_KEY, gson.toJson(tracksHistory)) }
    }

    fun clear() {
        val tracksCount = tracksHistory.size
        sharedPrefs.edit { remove(SEARCH_HISTORY_KEY) }
        tracksHistory.clear()
        adapter.notifyItemRangeRemoved(0, tracksCount)
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history_json"
        private const val HISTORY_MAX_SIZE = 10
    }
}