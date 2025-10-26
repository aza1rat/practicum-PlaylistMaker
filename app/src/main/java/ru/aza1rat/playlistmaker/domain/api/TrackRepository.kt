package ru.aza1rat.playlistmaker.domain.api

import ru.aza1rat.playlistmaker.domain.TrackSearchResult

interface TrackRepository {
    fun searchTracks(query: String): TrackSearchResult
}