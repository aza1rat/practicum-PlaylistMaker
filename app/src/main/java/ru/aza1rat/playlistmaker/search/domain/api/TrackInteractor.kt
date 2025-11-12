package ru.aza1rat.playlistmaker.search.domain.api

import ru.aza1rat.playlistmaker.search.domain.model.Track

interface TrackInteractor {
    fun searchTracks(query: String,consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?)
    }
}