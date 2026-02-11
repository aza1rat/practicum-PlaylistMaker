package ru.aza1rat.playlistmaker.media_library.domain.api

import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.search.domain.model.Track

interface FavouritesRepository {
    suspend fun add(track: Track)
    suspend fun delete(id: Int)
    fun list(): Flow<List<Track>>
}