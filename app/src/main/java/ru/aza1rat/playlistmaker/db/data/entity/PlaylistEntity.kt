package ru.aza1rat.playlistmaker.db.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlist_id")
    val id: Int = 0,
    val name: String,
    val description: String?,
    @ColumnInfo(name = "cover_path")
    val coverPath: String?
)