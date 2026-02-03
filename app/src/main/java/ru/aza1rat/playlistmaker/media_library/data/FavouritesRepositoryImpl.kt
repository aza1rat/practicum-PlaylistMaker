package ru.aza1rat.playlistmaker.media_library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.aza1rat.playlistmaker.db.data.converters.TrackDbConverter
import ru.aza1rat.playlistmaker.db.data.dao.TrackDao
import ru.aza1rat.playlistmaker.media_library.domain.api.FavouritesRepository
import ru.aza1rat.playlistmaker.search.domain.model.Track

class FavouritesRepositoryImpl(
    private val trackDao: TrackDao, private val converter: TrackDbConverter
) : FavouritesRepository {
    override suspend fun add(track: Track) {
        trackDao.insertTrack(converter.map(track))
    }

    override suspend fun delete(id: Int) {
        trackDao.deleteTrack(id)
    }

    override fun list(): Flow<List<Track>> {
        return trackDao.getTracksOrderedByAdding()
            .map { tracks -> tracks.map { trackDb -> converter.map(trackDb) } }
    }

}