package ru.aza1rat.playlistmaker.media_library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.aza1rat.playlistmaker.db.data.AppDatabase
import ru.aza1rat.playlistmaker.db.data.converters.TrackDbConverter
import ru.aza1rat.playlistmaker.media_library.domain.api.FavouritesRepository
import ru.aza1rat.playlistmaker.search.domain.model.Track

class FavouritesRepositoryImpl(
    private val database: AppDatabase,
    private val converter: TrackDbConverter
): FavouritesRepository {
    override suspend fun add(track: Track) {
        database.getTrackDao().insertTrack(converter.map(track))
    }

    override suspend fun delete(track: Track) {
        database.getTrackDao().deleteTrack(converter.map(track))
    }

    override fun list(): Flow<List<Track>> {
        return database.getTrackDao().getTracksByAddedAt().map {  tracks -> tracks.map { track -> converter.map(track) } }
    }

}