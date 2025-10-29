package ru.aza1rat.playlistmaker.domain.impl

import ru.aza1rat.playlistmaker.domain.api.SearchHistoryInteractor
import ru.aza1rat.playlistmaker.domain.api.SearchHistoryRepository
import ru.aza1rat.playlistmaker.domain.model.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun add(track: Track, trackInserted: (position:Int) -> Unit, trackRemoved: (position:Int) -> Unit) {
        repository.add(track, object: SearchHistoryRepository.SearchHistoryCallback {
            override fun onTrackRemoved(position: Int) {
                trackRemoved.invoke(position)
            }

            override fun onTrackInserted(position: Int) {
                trackInserted.invoke(position)
            }

        })
    }

    override fun clear(): Int {
        return repository.clear()
    }

    override fun get(): ArrayList<Track> {
        return repository.get()
    }
}