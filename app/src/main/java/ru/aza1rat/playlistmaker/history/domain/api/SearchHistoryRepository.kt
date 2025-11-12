package ru.aza1rat.playlistmaker.history.domain.api

import ru.aza1rat.playlistmaker.search.domain.model.Track

interface SearchHistoryRepository {
    fun add(track: Track, callback: SearchHistoryCallback)
    fun clear(): Int
    fun get(): ArrayList<Track>

    interface SearchHistoryCallback {
        fun onTrackRemoved(position: Int)
        fun onTrackInserted(position: Int)
    }
}