package ru.aza1rat.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date


data class TrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: Date?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?
)