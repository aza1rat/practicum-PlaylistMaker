package ru.aza1rat.playlistmaker.domain.impl

import ru.aza1rat.playlistmaker.domain.api.SearchHistoryInteractor
import ru.aza1rat.playlistmaker.domain.api.SearchHistoryRepository
import ru.aza1rat.playlistmaker.domain.model.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun add(
        track: Track, callback: SearchHistoryRepository.SearchHistoryCallback
    ) {
        repository.add(track, callback)
    }

    override fun clear(): Int {
        return repository.clear()
    }

    override fun get(): ArrayList<Track> {
        return repository.get()
    }
}