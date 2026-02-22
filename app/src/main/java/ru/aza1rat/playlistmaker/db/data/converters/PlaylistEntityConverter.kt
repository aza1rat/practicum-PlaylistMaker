package ru.aza1rat.playlistmaker.db.data.converters

import androidx.core.net.toUri
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistEntity
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist

class PlaylistEntityConverter {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.name,
            playlist.description,
            playlist.coverPath?.toUri()
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(name = playlist.name, description = playlist.description, coverPath = playlist.coverUri?.toString())
    }
}