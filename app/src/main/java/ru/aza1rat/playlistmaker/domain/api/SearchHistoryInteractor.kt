package ru.aza1rat.playlistmaker.domain.api

import ru.aza1rat.playlistmaker.domain.api.SearchHistoryRepository.SearchHistoryCallback
import ru.aza1rat.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {
    fun add(track: Track, trackInserted: (position: Int)->Unit, trackRemoved: (position: Int)->Unit)
    fun clear(): Int
    fun get(): ArrayList<Track>
}