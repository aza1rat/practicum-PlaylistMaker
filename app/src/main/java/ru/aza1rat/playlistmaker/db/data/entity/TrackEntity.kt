package ru.aza1rat.playlistmaker.db.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "track")
data class TrackEntity (
    @PrimaryKey
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
    val previewUrl: String?,
    @ColumnInfo(name = "added_at", defaultValue = "(strftime('%s', 'now'))")
    var addedAt: Long
)