package ru.aza1rat.playlistmaker.playlist.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.aza1rat.playlistmaker.db.data.converters.PlaylistEntityConverter
import ru.aza1rat.playlistmaker.db.data.dao.PlaylistDao
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist

class PlaylistRepositoryImpl(private val playlistDao: PlaylistDao, private val converter: PlaylistEntityConverter): PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(converter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().distinctUntilChanged()
            .map { playlists -> playlists.map { playlistEntity -> converter.map(playlistEntity) } }
    }
}