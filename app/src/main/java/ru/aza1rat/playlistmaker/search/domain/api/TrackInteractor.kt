package ru.aza1rat.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.search.domain.model.Track

interface TrackInteractor {
    fun searchTracks(query: String): Flow<List<Track>?>
}