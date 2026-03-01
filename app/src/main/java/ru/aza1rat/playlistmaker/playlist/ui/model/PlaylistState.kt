package ru.aza1rat.playlistmaker.playlist.ui.model

import ru.aza1rat.playlistmaker.playlist.domain.model.PlaylistTracks

sealed interface PlaylistState {
    data class Content(val playlistTracks: PlaylistTracks): PlaylistState
    data class TrackRemoved(val trackIndex: Int, val playlistTracks: PlaylistTracks): PlaylistState
    object Deleted: PlaylistState
}