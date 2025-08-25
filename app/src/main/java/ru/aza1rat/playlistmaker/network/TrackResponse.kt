package ru.aza1rat.playlistmaker.network

import ru.aza1rat.playlistmaker.data.Track

data class TrackResponse(
    val results: List<Track>
)
