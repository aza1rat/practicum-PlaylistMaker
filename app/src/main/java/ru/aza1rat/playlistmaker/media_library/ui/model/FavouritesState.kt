package ru.aza1rat.playlistmaker.media_library.ui.model

import ru.aza1rat.playlistmaker.search.domain.model.Track

sealed interface FavouritesState {
    object Empty : FavouritesState
    data class FavouritesContent(val favourites: List<Track>) : FavouritesState
}