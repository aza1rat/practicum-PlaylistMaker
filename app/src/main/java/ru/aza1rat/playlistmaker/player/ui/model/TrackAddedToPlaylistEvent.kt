package ru.aza1rat.playlistmaker.player.ui.model

sealed interface TrackAddedToPlaylistEvent {
    data class Added(val playlistName: String): TrackAddedToPlaylistEvent
    data class AlreadyExists(val playlistName: String): TrackAddedToPlaylistEvent
}