package ru.aza1rat.playlistmaker.db.data.converters

import ru.aza1rat.playlistmaker.db.data.entity.PlaylistEntity
import ru.aza1rat.playlistmaker.db.data.model.TrackDbModel
import ru.aza1rat.playlistmaker.playlist.domain.model.PlaylistTracks

class PlaylistTracksConverter(
    private val playlistEntityConverter: PlaylistEntityConverter,
    private val tracksConverter: TrackDbConverter
) {
    fun map(playlist: PlaylistEntity, tracks: List<TrackDbModel>): PlaylistTracks {
        return PlaylistTracks(
            playlistEntityConverter.map(playlist), tracks.map { tracksConverter.map(it) })
    }
}