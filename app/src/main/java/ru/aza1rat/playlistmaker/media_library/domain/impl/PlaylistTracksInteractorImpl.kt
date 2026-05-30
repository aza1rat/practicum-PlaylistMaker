package ru.aza1rat.playlistmaker.media_library.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.media_library.domain.api.PlaylistTracksInteractor
import ru.aza1rat.playlistmaker.media_library.domain.api.PlaylistTracksRepository
import ru.aza1rat.playlistmaker.media_library.domain.model.PlaylistTracksCount
import ru.aza1rat.playlistmaker.playlist.domain.model.PlaylistTracks
import ru.aza1rat.playlistmaker.search.domain.model.Track

class PlaylistTracksInteractorImpl(private val repository: PlaylistTracksRepository): PlaylistTracksInteractor {
    override fun listPlaylistWithTracksCount(): Flow<List<PlaylistTracksCount>> {
        return repository.listPlaylistWithTracksCount()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Int): Boolean {
        return repository.addTrackToPlaylist(track,playlistId)
    }

    override suspend fun getPlaylistTracks(playlistId: Int): PlaylistTracks {
        return repository.getPlaylistTracks(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        repository.deleteTrackFromPlaylist(playlistId,trackId)
    }
}