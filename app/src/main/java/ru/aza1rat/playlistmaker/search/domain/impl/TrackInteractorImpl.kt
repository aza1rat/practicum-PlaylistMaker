package ru.aza1rat.playlistmaker.search.domain.impl

import ru.aza1rat.playlistmaker.search.domain.api.TrackInteractor
import ru.aza1rat.playlistmaker.search.domain.api.TrackRepository
import ru.aza1rat.playlistmaker.util.data.Resource

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    override fun searchTracks(
        query: String,
        consumer: TrackInteractor.TrackConsumer
    ) {
        val thread = Thread {
            when (val resource = repository.searchTracks(query)) {
                is Resource.Success -> consumer.consume(resource.data)
                is Resource.Error -> consumer.consume(null)
            }
        }
        thread.start()
    }
}