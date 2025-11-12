package ru.aza1rat.playlistmaker.history.domain.api

import ru.aza1rat.playlistmaker.search.domain.model.Track

interface SearchHistoryInteractor {
    fun add(track: Track, callback: SearchHistoryRepository.SearchHistoryCallback)
    fun clear(): Int
    fun get(): ArrayList<Track>
}