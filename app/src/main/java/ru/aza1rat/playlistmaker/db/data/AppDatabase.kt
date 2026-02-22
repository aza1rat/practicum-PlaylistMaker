package ru.aza1rat.playlistmaker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.aza1rat.playlistmaker.db.data.dao.PlaylistDao
import ru.aza1rat.playlistmaker.db.data.dao.PlaylistTrackDao
import ru.aza1rat.playlistmaker.db.data.dao.TrackDao
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistEntity
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistTrackCrossRef
import ru.aza1rat.playlistmaker.db.data.entity.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackCrossRef::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTrackDao(): TrackDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getPlaylistTrackDao(): PlaylistTrackDao
}