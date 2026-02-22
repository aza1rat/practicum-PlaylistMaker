package ru.aza1rat.playlistmaker.media_library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.aza1rat.playlistmaker.db.data.converters.TrackDbConverter
import ru.aza1rat.playlistmaker.db.data.dao.PlaylistTrackDao
import ru.aza1rat.playlistmaker.db.data.dao.TrackDao
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistTrackCrossRef
import ru.aza1rat.playlistmaker.media_library.domain.api.PlaylistTracksRepository
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount
import ru.aza1rat.playlistmaker.search.domain.model.Track

class PlaylistTracksRepositoryImpl(private val playlistTrackDao: PlaylistTrackDao, private val trackDao: TrackDao, private val converter: TrackDbConverter) :
    PlaylistTracksRepository {
    override fun listPlaylistWithTracksCount(): Flow<List<PlaylistTracksCount>> {
        return playlistTrackDao.getPlaylistsTracksCount().distinctUntilChanged().map { playlistTracksDbList ->
            playlistTracksDbList.map { playlistTracksDb ->
                PlaylistTracksCountMapper.map(playlistTracksDb)
            }
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Int): Boolean {
        trackDao.insertTrackIfNotExists(converter.map(track,false))
        try {
            val result = playlistTrackDao.insertPlaylistTrack(
                PlaylistTrackCrossRef(playlistId, track.trackId)
            )
            return result > 0L
        }
        catch (_: Exception) {
            return false
        }
    }
}