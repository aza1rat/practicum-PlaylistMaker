package ru.aza1rat.playlistmaker.playlist.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistInteractor
import ru.aza1rat.playlistmaker.playlist.domain.api.PlaylistRepository
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist

class PlaylistInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }
}