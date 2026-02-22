package ru.aza1rat.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.db.data.entity.TrackEntity
import ru.aza1rat.playlistmaker.db.data.model.TrackDbModel

@Dao
interface TrackDao {
    @Upsert(entity = TrackEntity::class)
    suspend fun addTrackToFavourites(track: TrackDbModel)

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackIfNotExists(track: TrackDbModel)

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM track WHERE favourite = 1 ORDER BY added_at DESC")
    fun getFavouriteTracksOrderedByAdding(): Flow<List<TrackDbModel>>

    @Query("SELECT COUNT(*) FROM track WHERE track_id = :id AND favourite = 1")
    suspend fun getFavouriteTracksCountById(id: Int): Int

    @Query("UPDATE track SET favourite = 0 WHERE track_id = :id")
    suspend fun setTrackUnfavourite(id: Int)

    @Query("DELETE FROM track WHERE track.track_id = :id AND track.track_id NOT IN (SELECT track_id FROM playlist_track)")
    suspend fun deleteNonPlaylistTrack(id: Int)

    @Transaction
    suspend fun deleteFavouriteTrack(id: Int) {
        setTrackUnfavourite(id)
        deleteNonPlaylistTrack(id)
    }
}