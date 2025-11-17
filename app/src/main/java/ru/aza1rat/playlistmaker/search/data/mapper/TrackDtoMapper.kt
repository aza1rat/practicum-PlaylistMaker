package ru.aza1rat.playlistmaker.search.data.mapper

import android.util.Log
import ru.aza1rat.playlistmaker.search.data.dto.TrackDto
import ru.aza1rat.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TrackDtoMapper {
    fun mapToTrack(trackDto: TrackDto): Track {
        Log.d("TrackInfo", "name: ${trackDto.trackId}")
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTime = formatTrackTime(trackDto.trackTime),
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseYear = getReleaseYear(trackDto.releaseDate),
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl
        )
    }

    private fun formatTrackTime(trackTime: Long): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }

    private fun getReleaseYear(releaseDate: Date?): String? {
        releaseDate ?: return null
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(releaseDate)
    }
}