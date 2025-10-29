package ru.aza1rat.playlistmaker.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.aza1rat.playlistmaker.App
import ru.aza1rat.playlistmaker.data.SearchHistoryStorage
import ru.aza1rat.playlistmaker.domain.model.Track

class SharedPrefStorage private constructor (private val sharedPreferences: SharedPreferences,private val gson: Gson): SearchHistoryStorage {

    constructor(context: Context): this(
        sharedPreferences = context.getSharedPreferences(App.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE),
        gson = Gson()
    )

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