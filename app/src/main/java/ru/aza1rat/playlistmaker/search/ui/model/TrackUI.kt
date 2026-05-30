package ru.aza1rat.playlistmaker.search.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackUI(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseYear: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?
): Parcelable