package ru.aza1rat.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.util.domain.Resource

interface TrackRepository {
    fun searchTracks(query: String): Flow<Resource<List<Track>>>
}