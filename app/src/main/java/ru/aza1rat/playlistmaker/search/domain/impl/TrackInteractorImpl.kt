package ru.aza1rat.playlistmaker.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.aza1rat.playlistmaker.search.domain.api.TrackInteractor
import ru.aza1rat.playlistmaker.search.domain.api.TrackRepository
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.util.domain.Resource

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    override fun searchTracks(query: String): Flow<List<Track>?> {
        return repository.searchTracks(query).map { resource ->
            when(resource) {
                is Resource.Success -> resource.data
                is Resource.Error -> null
            }
        }
    }
}