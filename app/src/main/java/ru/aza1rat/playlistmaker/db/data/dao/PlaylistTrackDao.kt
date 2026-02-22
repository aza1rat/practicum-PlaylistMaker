package ru.aza1rat.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistTrackCrossRef
import ru.aza1rat.playlistmaker.db.data.model.PlaylistTracksCountDbModel

@Dao
interface PlaylistTrackDao {
    @Query("SELECT COUNT(*) FROM playlist_track WHERE playlist_id = :playlistId")
    suspend fun tracksCountByPlaylistId(playlistId: Int): Int

    @Insert(PlaylistTrackCrossRef::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTrackCrossRef): Long

    @Query("SELECT p.playlist_id, p.name, p.description, p.cover_path, COUNT(playlist_track.track_id) AS tracks_count " +
            "FROM playlist AS p LEFT JOIN playlist_track ON p.playlist_id = playlist_track.playlist_id GROUP BY p.playlist_id")
    fun getPlaylistsTracksCount(): Flow<List<PlaylistTracksCountDbModel>>
}