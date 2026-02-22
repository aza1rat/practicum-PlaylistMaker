package ru.aza1rat.playlistmaker.db.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistEntity

data class PlaylistTracksCountDbModel (
    @Embedded val playlist: PlaylistEntity,
    @ColumnInfo(name = "tracks_count") val tracksCount: Int
)