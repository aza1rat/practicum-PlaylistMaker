package ru.aza1rat.playlistmaker.db.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(tableName = "playlist_track",
    primaryKeys = ["playlist_id", "track_id"],
    foreignKeys = [
        ForeignKey(entity = PlaylistEntity::class, parentColumns = ["playlist_id"], childColumns = ["playlist_id"], onDelete = CASCADE),
        ForeignKey(entity = TrackEntity::class, parentColumns = ["track_id"], childColumns = ["track_id"], onDelete = CASCADE)
    ]
)
data class PlaylistTrackCrossRef (
    @ColumnInfo(name = "playlist_id")
    val playlistId: Int,
    @ColumnInfo(name = "track_id")
    val trackId: Int
)