package ru.aza1rat.playlistmaker.domain.api.interactor

import ru.aza1rat.playlistmaker.domain.model.TrackSearchResult

interface TrackInteractor {
    fun searchTracks(query: String,consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume(foundTracks: TrackSearchResult)
    }
}