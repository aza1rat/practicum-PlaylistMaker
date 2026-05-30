package ru.aza1rat.playlistmaker.playlist.ui.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistUI (
    val id: Int,
    val name: String,
    val description: String?,
    val coverUri: Uri?
): Parcelable