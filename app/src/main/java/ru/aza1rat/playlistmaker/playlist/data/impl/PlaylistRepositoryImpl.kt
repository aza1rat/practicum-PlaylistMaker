package ru.aza1rat.playlistmaker.playlist.data.impl

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.aza1rat.playlistmaker.db.data.converters.PlaylistEntityConverter
import ru.aza1rat.playlistmaker.db.data.dao.PlaylistDao
import ru.aza1rat.playlistmaker.media_library.ui.adapter.PlaylistTracksCountAdapter
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist

class PlaylistRepositoryImpl(private val playlistDao: PlaylistDao, private val converter: PlaylistEntityConverter): PlaylistRepository {
    override suspend fun addPlaylist(
        playlistName: String,
        playlistDescription: String?,
        playlistCover: Uri?
    ) {
        playlistDao.insertPlaylist(converter.map(Playlist(0, playlistName, playlistDescription, playlistCover)))
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().distinctUntilChanged()
            .map { playlists -> playlists.map { playlistEntity -> converter.map(playlistEntity) } }
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDao.deletePlaylistWithTracks(playlistId)
    }

    override suspend fun updatePlaylist(
        currentPlaylistId: Int,
        updateName: String,
        updateDescription: String?,
        updateCover: Uri?
    ) {
        playlistDao.updatePlaylist(currentPlaylistId, updateName, updateDescription, updateCover?.toString())
    }
}