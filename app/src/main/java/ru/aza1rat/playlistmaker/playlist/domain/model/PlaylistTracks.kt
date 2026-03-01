package ru.aza1rat.playlistmaker.playlist.domain.model

import ru.aza1rat.playlistmaker.search.domain.model.Track

data class PlaylistTracks (
    val playlist: Playlist,
    val tracks: List<Track>
)