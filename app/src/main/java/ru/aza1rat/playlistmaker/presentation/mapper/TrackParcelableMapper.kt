package ru.aza1rat.playlistmaker.presentation.mapper

import ru.aza1rat.playlistmaker.domain.model.Track
import ru.aza1rat.playlistmaker.presentation.model.TrackParcelable

object TrackParcelableMapper {
    fun mapToTrack(trackParcelable: TrackParcelable): Track {
        return Track(
            trackId = trackParcelable.trackId,
            trackName = trackParcelable.trackName,
            artistName = trackParcelable.artistName,
            trackTime = trackParcelable.trackTime,
            artworkUrl100 = trackParcelable.artworkUrl100,
            collectionName = trackParcelable.collectionName,
            releaseYear = trackParcelable.releaseYear,
            primaryGenreName = trackParcelable.primaryGenreName,
            country = trackParcelable.country,
            previewUrl = trackParcelable.previewUrl
        )
    }
}