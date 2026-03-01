package ru.aza1rat.playlistmaker.db.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track",
    foreignKeys = [
        ForeignKey(entity = PlaylistEntity::class, parentColumns = ["playlist_id"], childColumns = ["playlist_id"], onDelete = CASCADE),
        ForeignKey(entity = TrackEntity::class, parentColumns = ["track_id"], childColumns = ["track_id"], onDelete = CASCADE)
    ],
    indices = [Index(
        value = ["playlist_id", "track_id"],
        unique = true
    )]
)
data class PlaylistTrackCrossRef (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "playlist_id")
    val playlistId: Int,
    @ColumnInfo(name = "track_id")
    val trackId: Int
)