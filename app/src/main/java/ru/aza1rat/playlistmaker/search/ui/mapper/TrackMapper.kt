package ru.aza1rat.playlistmaker.search.ui.mapper

import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.model.TrackUI

object TrackMapper {
    fun mapToTrackParcelable(track: Track): TrackUI {
        return TrackUI(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseYear = track.releaseYear,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}