package ru.aza1rat.playlistmaker.media_library.domain.model

import android.net.Uri

data class PlaylistTracksCount(
    val id: Int,
    val name: String,
    val coverUri: Uri?,
    val count: Int
)