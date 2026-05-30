package ru.aza1rat.playlistmaker.playlist.domain.impl

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist

class PlaylistInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override suspend fun addPlaylist(
        playlistName: String,
        playlistDescription: String?,
        playlistCover: Uri?
    ) {
        repository.addPlaylist(playlistName, playlistDescription, playlistCover)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        repository.deletePlaylist(playlistId)
    }

    override suspend fun updatePlaylist(
        currentPlaylistId: Int,
        updateName: String,
        updateDescription: String?,
        updateCover: Uri?
    ) {
        repository.updatePlaylist(currentPlaylistId, updateName, updateDescription, updateCover)
    }
}