package ru.aza1rat.playlistmaker.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: Date?,
    val primaryGenreName: String,
    val country: String
) : Parcelable
{
    fun formatTrackTime(): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }

    fun getArtworkUrl512(): String? {
        return if (artworkUrl100.isNotEmpty()) {
            artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
        } else
            null
    }

    fun getReleaseYear(): String? {
        releaseDate ?: return null
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(releaseDate)
    }
}