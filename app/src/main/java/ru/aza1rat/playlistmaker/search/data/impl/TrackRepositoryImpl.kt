package ru.aza1rat.playlistmaker.search.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.aza1rat.playlistmaker.search.data.NetworkClient
import ru.aza1rat.playlistmaker.search.data.mapper.TrackDtoMapper
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.data.dto.TrackResponse
import ru.aza1rat.playlistmaker.search.data.dto.TrackSearchRequest
import ru.aza1rat.playlistmaker.search.domain.api.TrackRepository
import ru.aza1rat.playlistmaker.util.domain.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        if (response.resultCode == 200) {
            val tracks = (response as TrackResponse).results.map {
                TrackDtoMapper.mapToTrack(it)
            }
            emit(Resource.Success(tracks))
        }
        else
            emit(Resource.Error())
    }
}