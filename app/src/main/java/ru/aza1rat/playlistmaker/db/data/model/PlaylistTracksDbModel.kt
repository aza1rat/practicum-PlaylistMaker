package ru.aza1rat.playlistmaker.db.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistEntity
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistTrackCrossRef
import ru.aza1rat.playlistmaker.db.data.entity.TrackEntity

data class PlaylistTracksDbModel (
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlist_id",
        entityColumn = "track_id",
        associateBy = Junction(PlaylistTrackCrossRef::class)
    )
    val tracks: List<TrackEntity>
)