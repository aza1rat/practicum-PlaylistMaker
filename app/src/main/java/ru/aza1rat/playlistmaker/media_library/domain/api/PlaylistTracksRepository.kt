package ru.aza1rat.playlistmaker.media_library.domain.api

import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount
import ru.aza1rat.playlistmaker.playlist.domain.model.PlaylistTracks
import ru.aza1rat.playlistmaker.search.domain.model.Track

interface PlaylistTracksRepository {
    fun listPlaylistWithTracksCount(): Flow<List<PlaylistTracksCount>>
    suspend fun addTrackToPlaylist(track: Track, playlistId: Int): Boolean
    suspend fun getPlaylistTracks(playlistId: Int): PlaylistTracks
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)
}