package ru.aza1rat.playlistmaker.db.data.model

import androidx.room.ColumnInfo

data class TrackDbModel (
    val id: Int,
    val name: String,
    val artist: String,
    val time: String,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl100: String,
    val collection: String?,
    @ColumnInfo(name = "release_year")
    val releaseYear: String?,
    @ColumnInfo(name = "primary_genre")
    val primaryGenreName: String,
    val country: String,
    @ColumnInfo(name = "preview_url")
    val previewUrl: String?
)