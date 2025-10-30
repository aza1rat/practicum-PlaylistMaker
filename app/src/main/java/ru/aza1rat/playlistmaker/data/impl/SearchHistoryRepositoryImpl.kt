package ru.aza1rat.playlistmaker.data.impl

import ru.aza1rat.playlistmaker.data.SearchHistoryStorage
import ru.aza1rat.playlistmaker.domain.api.repository.SearchHistoryRepository
import ru.aza1rat.playlistmaker.domain.model.Track

class SearchHistoryRepositoryImpl (
    private val searchHistoryStorage: SearchHistoryStorage,
    private val tracksHistory: ArrayList<Track> = searchHistoryStorage.load()
): SearchHistoryRepository {

    override fun add(
        track: Track,
        callback: SearchHistoryRepository.SearchHistoryCallback
    ) {
        for (i in 0 until tracksHistory.size) {
            if (tracksHistory[i].trackId == track.trackId) {
                tracksHistory.removeAt(i)
                callback.onTrackRemoved(i)
                break
            }
        }
        if (tracksHistory.size == HISTORY_MAX_SIZE) {
            tracksHistory.removeAt(HISTORY_MAX_SIZE - 1)
            callback.onTrackRemoved(HISTORY_MAX_SIZE - 1)
        }
        tracksHistory.add(0, track)
        searchHistoryStorage.save(tracksHistory)
        callback.onTrackInserted(0)

    }

    override fun clear(): Int {
        val tracksCount = tracksHistory.size
        tracksHistory.clear()
        searchHistoryStorage.clear()
        return tracksCount
    }

    override fun get(): ArrayList<Track> {
        return tracksHistory
    }

    companion object {
        private const val HISTORY_MAX_SIZE = 10
    }
}