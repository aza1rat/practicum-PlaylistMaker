package ru.aza1rat.playlistmaker.di

import org.koin.dsl.module
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryInteractor
import ru.aza1rat.playlistmaker.history.domain.impl.SearchHistoryInteractorImpl
import ru.aza1rat.playlistmaker.media_library.domain.api.FavouritesInteractor
import ru.aza1rat.playlistmaker.media_library.domain.impl.FavouritesInteractorImpl
import ru.aza1rat.playlistmaker.player.domain.api.PlayerInteractor
import ru.aza1rat.playlistmaker.player.domain.impl.PlayerInteractorImpl
import ru.aza1rat.playlistmaker.search.domain.api.TrackInteractor
import ru.aza1rat.playlistmaker.search.domain.impl.TrackInteractorImpl
import ru.aza1rat.playlistmaker.settings.domain.ThemeInteractor
import ru.aza1rat.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import ru.aza1rat.playlistmaker.sharing.domain.api.SharingInteractor
import ru.aza1rat.playlistmaker.sharing.domain.impl.SharingInteractorImpl

val interactorModule = module {
    factory<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<FavouritesInteractor> {
        FavouritesInteractorImpl(get())
    }
}