package ru.aza1rat.playlistmaker.presentation.mapper

import ru.aza1rat.playlistmaker.domain.model.Track
import ru.aza1rat.playlistmaker.presentation.model.TrackUI

object TrackUIMapper {
    fun mapToTrack(trackUI: TrackUI): Track {
        return Track(
            trackId = trackUI.trackId,
            trackName = trackUI.trackName,
            artistName = trackUI.artistName,
            trackTime = trackUI.trackTime,
            artworkUrl100 = trackUI.artworkUrl100,
            collectionName = trackUI.collectionName,
            releaseYear = trackUI.releaseYear,
            primaryGenreName = trackUI.primaryGenreName,
            country = trackUI.country,
            previewUrl = trackUI.previewUrl
        )
    }
}