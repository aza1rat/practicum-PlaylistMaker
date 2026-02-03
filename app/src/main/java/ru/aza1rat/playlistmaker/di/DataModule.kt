package ru.aza1rat.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.aza1rat.playlistmaker.db.data.AppDatabase
import ru.aza1rat.playlistmaker.db.data.dao.TrackDao
import ru.aza1rat.playlistmaker.history.data.SharedPrefsStorageClient
import ru.aza1rat.playlistmaker.history.data.StorageClient
import ru.aza1rat.playlistmaker.search.data.NetworkClient
import ru.aza1rat.playlistmaker.search.data.RetrofitNetworkClient
import ru.aza1rat.playlistmaker.search.data.SearchAPI
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.settings.data.BaseThemeControl
import ru.aza1rat.playlistmaker.settings.data.ThemeControl

private const val SHARED_PREFERENCES_NAME = "playlistmaker_preferences"
private const val SEARCH_HISTORY_KEY = "search_history_json"
private const val ITUNES_BASE_URL = "https://itunes.apple.com"

val dataModule = module {
    single<BaseThemeControl> {
        ThemeControl(context = androidContext(), sharedPreferences = get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(searchService = get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }

    single<StorageClient<List<Track>>> {
        SharedPrefsStorageClient(
            sharedPreferences = get(),
            gson = get(),
            dataKey = SEARCH_HISTORY_KEY,
            type = object : TypeToken<List<Track>>() {}.type
        )
    }

    single<SearchAPI> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchAPI::class.java)
    }

    factory<Gson> {
        Gson()
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single<TrackDao> {
        get<AppDatabase>().getTrackDao()
    }

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}