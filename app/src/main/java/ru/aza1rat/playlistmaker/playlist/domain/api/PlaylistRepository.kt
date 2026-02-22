package ru.aza1rat.playlistmaker.playlist.domain.api

import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
}