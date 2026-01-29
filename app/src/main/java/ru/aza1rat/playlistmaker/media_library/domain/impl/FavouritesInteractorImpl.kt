package ru.aza1rat.playlistmaker.media_library.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.media_library.domain.api.FavouritesInteractor
import ru.aza1rat.playlistmaker.media_library.domain.api.FavouritesRepository
import ru.aza1rat.playlistmaker.search.domain.model.Track

class FavouritesInteractorImpl(private val repository: FavouritesRepository): FavouritesInteractor {
    override suspend fun add(track: Track) {
        repository.add(track)
    }

    override suspend fun delete(track: Track) {
        repository.delete(track)
    }

    override fun list(): Flow<List<Track>> {
        return repository.list()
    }
}