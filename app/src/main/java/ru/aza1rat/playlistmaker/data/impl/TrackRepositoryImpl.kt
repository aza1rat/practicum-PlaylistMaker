package ru.aza1rat.playlistmaker.data.impl

import ru.aza1rat.playlistmaker.data.NetworkClient
import ru.aza1rat.playlistmaker.data.dto.TrackResponse
import ru.aza1rat.playlistmaker.data.dto.TrackSearchRequest
import ru.aza1rat.playlistmaker.data.mapper.TrackDtoMapper
import ru.aza1rat.playlistmaker.domain.model.TrackSearchResult
import ru.aza1rat.playlistmaker.domain.api.repository.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(query: String): TrackSearchResult {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        if (response.resultCode == 200) {
            val tracks = (response as TrackResponse).results.map {
                TrackDtoMapper.mapToTrack(it)
            }
            return TrackSearchResult(response.resultCode, tracks)
        }
        else
            return TrackSearchResult(response.resultCode, emptyList())
    }
}