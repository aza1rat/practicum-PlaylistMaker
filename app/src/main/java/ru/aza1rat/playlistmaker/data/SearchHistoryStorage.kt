package ru.aza1rat.playlistmaker.data

import ru.aza1rat.playlistmaker.domain.model.Track

interface SearchHistoryStorage {
    fun load(): ArrayList<Track>
    fun save(history: ArrayList<Track>)
    fun clear()
}