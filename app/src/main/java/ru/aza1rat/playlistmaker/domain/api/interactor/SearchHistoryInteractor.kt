package ru.aza1rat.playlistmaker.domain.api.interactor

import ru.aza1rat.playlistmaker.domain.api.repository.SearchHistoryRepository
import ru.aza1rat.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {
    fun add(track: Track, callback: SearchHistoryRepository.SearchHistoryCallback)
    fun clear(): Int
    fun get(): ArrayList<Track>
}