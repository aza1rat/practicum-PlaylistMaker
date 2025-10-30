package ru.aza1rat.playlistmaker.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.aza1rat.playlistmaker.data.SearchHistoryStorage
import ru.aza1rat.playlistmaker.domain.model.Track

class SharedPrefStorage (private val sharedPreferences: SharedPreferences,private val gson: Gson): SearchHistoryStorage {

    override fun load(): ArrayList<Track> {
        val searchHistory = sharedPreferences.getString(SEARCH_HISTORY_KEY, "")
        if (searchHistory.isNullOrEmpty()) return arrayListOf()
        else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            return gson.fromJson(searchHistory, type)
        }
    }

    override fun save(history: ArrayList<Track>) {
        sharedPreferences.edit { putString(SEARCH_HISTORY_KEY, gson.toJson(history)) }
    }

    override fun clear() {
        sharedPreferences.edit { remove(SEARCH_HISTORY_KEY) }
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history_json"
    }
}