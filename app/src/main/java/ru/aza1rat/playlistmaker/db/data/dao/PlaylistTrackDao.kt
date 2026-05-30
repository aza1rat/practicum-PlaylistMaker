package ru.aza1rat.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistTrackCrossRef
import ru.aza1rat.playlistmaker.db.data.model.PlaylistTracksCountDbModel
import ru.aza1rat.playlistmaker.db.data.model.TrackDbModel

@Dao
interface PlaylistTrackDao {
    @Insert(PlaylistTrackCrossRef::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTrackCrossRef): Long

    @Query("SELECT p.playlist_id, p.name, p.description, p.cover_path, COUNT(playlist_track.track_id) AS tracks_count " +
            "FROM playlist AS p LEFT JOIN playlist_track ON p.playlist_id = playlist_track.playlist_id GROUP BY p.playlist_id")
    fun getPlaylistsTracksCount(): Flow<List<PlaylistTracksCountDbModel>>

    @Query("SELECT track.* FROM playlist_track JOIN track ON playlist_track.track_id = track.track_id WHERE playlist_id = :playlistId ORDER BY id DESC")
    suspend fun getTracksFromPlaylist(playlistId: Int): List<TrackDbModel>

    @Query("DELETE FROM playlist_track WHERE playlist_id = :playlistId AND track_id = :trackId")
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)

    @Query("DELETE FROM track WHERE track_id = :trackId AND favourite = 0 AND track_id" +
            " NOT IN (SELECT track_id FROM playlist_track)")
    suspend fun deleteNonFavouriteNonPlaylistTrack(trackId: Int)

    @Transaction
    suspend fun deleteNonFavouriteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        deleteTrackFromPlaylist(playlistId, trackId)
        deleteNonFavouriteNonPlaylistTrack(trackId)
    }
}