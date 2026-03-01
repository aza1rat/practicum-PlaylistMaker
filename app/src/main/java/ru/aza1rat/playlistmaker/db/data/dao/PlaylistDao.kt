package ru.aza1rat.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.db.data.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist WHERE playlist_id = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("UPDATE playlist SET name = :updateName, description = :updateDescription, cover_path = :updateCover WHERE playlist_id = :currentPlaylistId")
    suspend fun updatePlaylist(currentPlaylistId: Int, updateName: String, updateDescription: String?, updateCover: String?)

    @Query("DELETE FROM track WHERE track.favourite = 0 AND NOT EXISTS (SELECT 1 FROM playlist_track WHERE track.track_id = playlist_track.track_id)")
    suspend fun deleteUnfavouriteNonPlaylistTracks()

    @Query("DELETE FROM playlist WHERE playlist_id = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Transaction
    suspend fun deletePlaylistWithTracks(playlistId: Int) {
        deletePlaylist(playlistId)
        deleteUnfavouriteNonPlaylistTracks()
    }
}