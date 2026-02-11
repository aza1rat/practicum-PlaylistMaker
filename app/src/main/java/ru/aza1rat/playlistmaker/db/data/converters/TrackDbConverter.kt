package ru.aza1rat.playlistmaker.db.data.converters

import ru.aza1rat.playlistmaker.db.data.model.TrackDbModel
import ru.aza1rat.playlistmaker.search.domain.model.Track

class TrackDbConverter {
    fun map(track: Track): TrackDbModel {
        return TrackDbModel(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: TrackDbModel): Track {
        return Track(
            track.id,
            track.name,
            track.artist,
            track.time,
            track.artworkUrl100,
            track.collection,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}