package ru.aza1rat.playlistmaker.domain.api.repository

import ru.aza1rat.playlistmaker.domain.model.TrackSearchResult

interface TrackRepository {
    fun searchTracks(query: String): TrackSearchResult
}