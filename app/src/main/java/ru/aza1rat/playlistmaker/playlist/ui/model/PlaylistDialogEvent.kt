package ru.aza1rat.playlistmaker.playlist.ui.model

interface PlaylistDialogEvent {
    object UnableToShare : PlaylistDialogEvent
    data class ShareTracksMessage(val tracksCount: Int, val message: (tracksCountStr: String)->String): PlaylistDialogEvent
    object DeletePlaylistDialog: PlaylistDialogEvent
}