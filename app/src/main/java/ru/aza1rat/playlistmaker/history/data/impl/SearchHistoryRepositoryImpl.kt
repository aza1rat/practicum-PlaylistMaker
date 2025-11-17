package ru.aza1rat.playlistmaker.history.data.impl

import ru.aza1rat.playlistmaker.history.data.StorageClient
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryRepository
import ru.aza1rat.playlistmaker.search.domain.model.Track

class SearchHistoryRepositoryImpl (
    private val searchHistoryStorageClient: StorageClient<ArrayList<Track>>,
    private val tracksHistory: ArrayList<Track> = searchHistoryStorageClient.getData() ?: ArrayList()
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
        searchHistoryStorageClient.storeData(tracksHistory)
        callback.onTrackInserted(0)

    }

    override fun clear(): Int {
        val tracksCount = tracksHistory.size
        tracksHistory.clear()
        searchHistoryStorageClient.clearData()
        return tracksCount
    }

    override fun get(): List<Track> {
        return tracksHistory
    }

    companion object {
        private const val HISTORY_MAX_SIZE = 10
    }
}