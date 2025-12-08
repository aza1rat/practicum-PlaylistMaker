package ru.aza1rat.playlistmaker.search.domain.api

import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.util.domain.Resource

interface TrackRepository {
    fun searchTracks(query: String): Resource<List<Track>>
}