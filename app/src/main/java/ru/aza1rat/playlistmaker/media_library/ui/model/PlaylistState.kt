package ru.aza1rat.playlistmaker.media_library.ui.model

import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount

sealed interface PlaylistState {
    object Empty : PlaylistState
    data class Content(val playlists: List<PlaylistTracksCount>) : PlaylistState
}