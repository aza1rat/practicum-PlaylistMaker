package ru.aza1rat.playlistmaker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Track (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseYear: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
): Parcelable {
    fun getArtworkUrl512(): String? {
        return if (artworkUrl100.isNotEmpty()) {
            artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
        } else
            null
    }
}