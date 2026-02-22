package ru.aza1rat.playlistmaker.player.ui.model

import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount

sealed interface PlaylistSheetState {
    object Empty : PlaylistSheetState
    data class Content(val playlists: List<PlaylistTracksCount>) : PlaylistSheetState
}