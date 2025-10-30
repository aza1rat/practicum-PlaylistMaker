package ru.aza1rat.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import ru.aza1rat.playlistmaker.data.BaseThemeControl
import ru.aza1rat.playlistmaker.data.NetworkClient
import ru.aza1rat.playlistmaker.data.SearchHistoryStorage
import ru.aza1rat.playlistmaker.data.impl.PlayerRepositoryImpl
import ru.aza1rat.playlistmaker.data.impl.SearchHistoryRepositoryImpl
import ru.aza1rat.playlistmaker.data.impl.ThemeRepositoryImpl
import ru.aza1rat.playlistmaker.data.impl.TrackRepositoryImpl
import ru.aza1rat.playlistmaker.data.network.RetrofitNetworkClient
import ru.aza1rat.playlistmaker.data.settings.ThemeControl
import ru.aza1rat.playlistmaker.data.storage.SharedPrefStorage
import ru.aza1rat.playlistmaker.domain.api.interactor.PlayerInteractor
import ru.aza1rat.playlistmaker.domain.api.interactor.SearchHistoryInteractor
import ru.aza1rat.playlistmaker.domain.api.interactor.ThemeInteractor
import ru.aza1rat.playlistmaker.domain.api.interactor.TrackInteractor
import ru.aza1rat.playlistmaker.domain.api.repository.PlayerRepository
import ru.aza1rat.playlistmaker.domain.api.repository.SearchHistoryRepository
import ru.aza1rat.playlistmaker.domain.api.repository.ThemeRepository
import ru.aza1rat.playlistmaker.domain.api.repository.TrackRepository
import ru.aza1rat.playlistmaker.domain.impl.PlayerInteractorImpl
import ru.aza1rat.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import ru.aza1rat.playlistmaker.domain.impl.ThemeInteractorImpl
import ru.aza1rat.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {
    const val SHARED_PREFERENCES_NAME = "playlistmaker_preferences"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appContext: Context

    fun init(appContext: Context) {
        this.appContext = appContext
        this.sharedPreferences = appContext.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
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
        return SearchHistoryRepositoryImpl(createSearchHistoryStorage())
    }

    private fun getThemeRepository(themeControl: BaseThemeControl): ThemeRepository {
        return ThemeRepositoryImpl(themeControl)
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(createNetworkClient())
    }

    private fun createMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun createGson(): Gson {
        return Gson()
    }

    private fun createSearchHistoryStorage(): SearchHistoryStorage {
        return SharedPrefStorage(sharedPreferences,createGson())
    }

    fun createThemeControl(): BaseThemeControl {
        return ThemeControl(appContext,sharedPreferences)
    }

    private fun createNetworkClient(): NetworkClient {
        return RetrofitNetworkClient()
    }
}