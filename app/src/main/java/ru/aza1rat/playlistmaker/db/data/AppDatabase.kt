package ru.aza1rat.playlistmaker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.aza1rat.playlistmaker.db.data.dao.TrackDao
import ru.aza1rat.playlistmaker.db.data.entity.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTrackDao(): TrackDao
}