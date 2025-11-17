package ru.aza1rat.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.aza1rat.playlistmaker.history.data.SharedPrefsStorageClient
import ru.aza1rat.playlistmaker.history.data.StorageClient
import ru.aza1rat.playlistmaker.history.data.impl.SearchHistoryRepositoryImpl
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryInteractor
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryRepository
import ru.aza1rat.playlistmaker.history.domain.impl.SearchHistoryInteractorImpl
import ru.aza1rat.playlistmaker.player.data.impl.PlayerRepositoryImpl
import ru.aza1rat.playlistmaker.player.domain.api.PlayerInteractor
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository
import ru.aza1rat.playlistmaker.player.domain.impl.PlayerInteractorImpl
import ru.aza1rat.playlistmaker.search.data.NetworkClient
import ru.aza1rat.playlistmaker.search.data.RetrofitNetworkClient
import ru.aza1rat.playlistmaker.search.data.SearchAPI
import ru.aza1rat.playlistmaker.search.data.impl.TrackRepositoryImpl
import ru.aza1rat.playlistmaker.search.domain.api.TrackInteractor
import ru.aza1rat.playlistmaker.search.domain.api.TrackRepository
import ru.aza1rat.playlistmaker.search.domain.impl.TrackInteractorImpl
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.settings.data.BaseThemeControl
import ru.aza1rat.playlistmaker.settings.data.ThemeControl
import ru.aza1rat.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import ru.aza1rat.playlistmaker.settings.domain.ThemeInteractor
import ru.aza1rat.playlistmaker.settings.domain.ThemeRepository
import ru.aza1rat.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import ru.aza1rat.playlistmaker.sharing.domain.api.ExternalNavigator
import ru.aza1rat.playlistmaker.sharing.data.ExternalNavigatorImpl
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingInteractor
import ru.aza1rat.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import ru.aza1rat.playlistmaker.sharing.domain.model.EmailData

object Creator {
    private const val SHARED_PREFERENCES_NAME = "playlistmaker_preferences"
    private const val SEARCH_HISTORY_KEY = "search_history_json"
    private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    private val retrofit: Retrofit by lazy { createRetrofit() }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appContext: Context

    fun init(appContext: Context) {
        this.appContext = appContext
        this.sharedPreferences = appContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun provideSharingInteractor(sharingLink: String, termsLink: String, email: EmailData): SharingInteractor {
        return SharingInteractorImpl(
            createExternalNavigator(),
            sharingLink,
            termsLink,
            email
        )
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    fun provideThemeInteractor(themeControl: BaseThemeControl): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(themeControl))
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(createMediaPlayer())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(createStorageClient())
    }

    private fun getThemeRepository(themeControl: BaseThemeControl): ThemeRepository {
        return ThemeRepositoryImpl(themeControl)
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(createNetworkClient())
    }

    fun getSupportEmail(email:String,subject:String,text:String): EmailData {
        return EmailData(email, subject, text)
    }

    private fun createExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(appContext)
    }

    private fun createMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun createGson(): Gson {
        return Gson()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createSearchService(): SearchAPI {
        return retrofit.create(SearchAPI::class.java)
    }

    private fun createStorageClient(): StorageClient<ArrayList<Track>> {
        return SharedPrefsStorageClient(
            sharedPreferences,
            createGson(),
            SEARCH_HISTORY_KEY,
            object : TypeToken<ArrayList<Track>>() {}.type
        )
    }

    fun createThemeControl(): BaseThemeControl {
        return ThemeControl(appContext, sharedPreferences)
    }

    private fun createNetworkClient(): NetworkClient {
        return RetrofitNetworkClient(createSearchService())
    }
}