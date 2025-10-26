package ru.aza1rat.playlistmaker.data.impl

import ru.aza1rat.playlistmaker.data.NetworkClient
import ru.aza1rat.playlistmaker.data.dto.TrackResponse
import ru.aza1rat.playlistmaker.data.dto.TrackSearchRequest
import ru.aza1rat.playlistmaker.domain.TrackSearchResult
import ru.aza1rat.playlistmaker.domain.api.TrackRepository
import ru.aza1rat.playlistmaker.domain.model.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(query: String): TrackSearchResult {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        if (response.resultCode == 200) {
            val tracks = (response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.formatTrackTime(),
                    it.artworkUrl100,
                    it.collectionName,
                    it.getReleaseYear(),
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
            return TrackSearchResult(response.resultCode, tracks)
        }
        else
            return TrackSearchResult(response.resultCode, emptyList())
    }
}