package ru.aza1rat.playlistmaker.data

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String
)
{
    fun formatTrackTime(): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    }
}