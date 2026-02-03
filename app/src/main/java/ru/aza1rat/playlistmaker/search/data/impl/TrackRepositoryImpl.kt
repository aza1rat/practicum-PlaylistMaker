package ru.aza1rat.playlistmaker.search.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ru.aza1rat.playlistmaker.db.data.dao.TrackDao
import ru.aza1rat.playlistmaker.search.data.NetworkClient
import ru.aza1rat.playlistmaker.search.data.mapper.TrackDtoMapper
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.data.dto.TrackResponse
import ru.aza1rat.playlistmaker.search.data.dto.TrackSearchRequest
import ru.aza1rat.playlistmaker.search.domain.api.TrackRepository
import ru.aza1rat.playlistmaker.util.domain.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient, private val trackDao: TrackDao) : TrackRepository {
    override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        if (response.resultCode == 200) {
            val tracks = (response as TrackResponse).results.map {
                TrackDtoMapper.mapToTrack(it)
            }
            if (tracks.isNotEmpty()) {
                trackDao.getTracksIds()
            }
            emit(Resource.Success(tracks))
        }
        else
            emit(Resource.Error())
    }
}