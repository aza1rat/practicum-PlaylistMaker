package ru.aza1rat.playlistmaker.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.aza1rat.playlistmaker.db.data.converters.PlaylistEntityConverter
import ru.aza1rat.playlistmaker.db.data.converters.PlaylistTracksConverter
import ru.aza1rat.playlistmaker.db.data.converters.TrackDbConverter
import ru.aza1rat.playlistmaker.history.data.impl.SearchHistoryRepositoryImpl
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryRepository
import ru.aza1rat.playlistmaker.media_library.data.FavouritesRepositoryImpl
import ru.aza1rat.playlistmaker.media_library.data.PlaylistTracksRepositoryImpl
import ru.aza1rat.playlistmaker.media_library.domain.api.FavouritesRepository
import ru.aza1rat.playlistmaker.media_library.domain.api.PlaylistTracksRepository
import ru.aza1rat.playlistmaker.player.data.impl.PlayerRepositoryImpl
import ru.aza1rat.playlistmaker.player.domain.api.PlayerRepository
import ru.aza1rat.playlistmaker.playlist.data.impl.FileStorageRepositoryImpl
import ru.aza1rat.playlistmaker.playlist.data.impl.PlaylistRepositoryImpl
import ru.aza1rat.playlistmaker.playlist.domain.api.FileStorageRepository
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistRepository
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
        PlayerRepositoryImpl(get(),get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
    }

    factory {
        TrackDbConverter()
    }

    factory<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }
    factory<FileStorageRepository> {
        FileStorageRepositoryImpl(get())
    }
    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(get(),get())
    }
    factory {
        PlaylistEntityConverter()
    }
    factory {
        PlaylistTracksConverter(get(), get())
    }
    factory<PlaylistTracksRepository> {
        PlaylistTracksRepositoryImpl(get(),get(),get(),get(), get())
    }
}