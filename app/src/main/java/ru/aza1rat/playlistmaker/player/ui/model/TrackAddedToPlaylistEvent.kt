package ru.aza1rat.playlistmaker.player.ui.model

import ru.aza1rat.playlistmaker.search.domain.model.Track

sealed class TrackAddedToPlaylistEvent(playlistName: String) {
    data class Added(val playlistName: String): TrackAddedToPlaylistEvent(playlistName)
    data class AlreadyExists(val playlistName: String): TrackAddedToPlaylistEvent(playlistName)
}