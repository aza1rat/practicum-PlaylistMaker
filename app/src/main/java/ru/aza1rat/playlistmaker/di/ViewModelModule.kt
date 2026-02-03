package ru.aza1rat.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.aza1rat.playlistmaker.media_library.ui.view_model.FavouriteTracksViewModel
import ru.aza1rat.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import ru.aza1rat.playlistmaker.player.ui.view_model.PlayerViewModel
import ru.aza1rat.playlistmaker.search.ui.view_model.SearchViewModel
import ru.aza1rat.playlistmaker.settings.ui.view_model.SettingsViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(),get())
    }
    viewModel {
        PlayerViewModel(get(),get())
    }
    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        FavouriteTracksViewModel(get())
    }
    viewModel {
        PlaylistsViewModel()
    }
}