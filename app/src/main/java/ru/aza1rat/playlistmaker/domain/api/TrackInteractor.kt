package ru.aza1rat.playlistmaker.domain.api

import ru.aza1rat.playlistmaker.domain.TrackSearchResult

interface TrackInteractor {
    fun searchTracks(query: String,consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume(foundTracks: TrackSearchResult)
    }
}