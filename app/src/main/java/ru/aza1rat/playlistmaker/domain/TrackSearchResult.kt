package ru.aza1rat.playlistmaker.domain

import ru.aza1rat.playlistmaker.domain.model.Track

data class TrackSearchResult (val result: Int, val tracks: List<Track>)