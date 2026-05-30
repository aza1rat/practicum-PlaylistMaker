package ru.aza1rat.playlistmaker.playlist.ui.mapper

import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist
import ru.aza1rat.playlistmaker.playlist.ui.model.PlaylistUI

object PlaylistMapper {
    fun map(playlist: Playlist): PlaylistUI {
        return PlaylistUI(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverUri
        )
    }
    fun map(playlist: PlaylistUI): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverUri
        )
    }
}