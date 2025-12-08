package ru.aza1rat.playlistmaker.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.aza1rat.playlistmaker.history.data.impl.SearchHistoryRepositoryImpl
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryRepository
import ru.aza1rat.playlistmaker.player.data.impl.PlayerRepositoryImpl
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository
import ru.aza1rat.playlistmaker.search.data.impl.TrackRepositoryImpl
import ru.aza1rat.playlistmaker.search.domain.api.TrackRepository
import ru.aza1rat.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import ru.aza1rat.playlistmaker.settings.domain.ThemeRepository
import ru.aza1rat.playlistmaker.sharing.data.ExternalNavigatorImpl
import ru.aza1rat.playlistmaker.sharing.domain.api.ExternalNavigator

val repositoryModule = module {
    factory<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }

    factory<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
    }
}