package ru.aza1rat.playlistmaker.playlist.ui.model

import android.net.Uri

sealed interface CreatePlaylistState {
    data class Created(val playlistName: String) : CreatePlaylistState
    data class Content(val imageUri: Uri): CreatePlaylistState
}