package ru.aza1rat.playlistmaker.domain.impl

import ru.aza1rat.playlistmaker.domain.api.interactor.TrackInteractor
import ru.aza1rat.playlistmaker.domain.api.repository.TrackRepository

class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    override fun searchTracks(
        query: String,
        consumer: TrackInteractor.TrackConsumer
    ) {
        val thread = Thread {
            consumer.consume(repository.searchTracks(query))
        }
        thread.start()
    }
}