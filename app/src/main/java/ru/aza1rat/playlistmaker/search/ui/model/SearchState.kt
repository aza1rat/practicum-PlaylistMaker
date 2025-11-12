package ru.aza1rat.playlistmaker.search.ui.model

import ru.aza1rat.playlistmaker.search.domain.model.Track

sealed interface SearchState {
    object Idle : SearchState
    object Loading : SearchState
    object Empty : SearchState
    object Error : SearchState
    data class TracksContent(val tracks: List<Track>) : SearchState
    object SearchHistory : SearchState
}