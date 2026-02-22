package ru.aza1rat.playlistmaker.media_library.data

import androidx.core.net.toUri
import ru.aza1rat.playlistmaker.db.data.model.PlaylistTracksCountDbModel
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount

object PlaylistTracksCountMapper {
    fun map(playlistTracksCountDbModel: PlaylistTracksCountDbModel): PlaylistTracksCount {
        return PlaylistTracksCount(
            id = playlistTracksCountDbModel.playlist.id,
            name = playlistTracksCountDbModel.playlist.name,
            coverUri = playlistTracksCountDbModel.playlist.coverPath?.toUri(),
            count = playlistTracksCountDbModel.tracksCount
        )
    }
}