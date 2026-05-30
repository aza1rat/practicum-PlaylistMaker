package ru.aza1rat.playlistmaker.playlist.domain.api

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.playlist.domain.model.Playlist

interface PlaylistInteractor {
    suspend fun addPlaylist(playlistName: String, playlistDescription: String?, playlistCover: Uri?)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun deletePlaylist(playlistId: Int)
    suspend fun updatePlaylist(currentPlaylistId: Int, updateName: String, updateDescription: String?, updateCover: Uri?)
}