package ru.aza1rat.playlistmaker.history.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import java.lang.reflect.Type

class SharedPrefsStorageClient<T>(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val dataKey: String,
    private val type: Type
) : StorageClient<T> {
    override fun storeData(data: T) {
        sharedPreferences.edit { putString(dataKey, gson.toJson(data, type)) }
    }

    override fun getData(): T? {
        val searchHistory = sharedPreferences.getString(dataKey, "")
        return if (searchHistory.isNullOrEmpty()) null
        else gson.fromJson(searchHistory, type)

    }

    override fun clearData() {
        sharedPreferences.edit { remove(dataKey) }
    }
}