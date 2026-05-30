package ru.aza1rat.playlistmaker.playlist.domain.model

import android.net.Uri

data class Playlist(
    val id: Int,
    val name: String,
    val description: String?,
    val coverUri: Uri?
)