package ru.aza1rat.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.aza1rat.playlistmaker.db.data.entity.TrackEntity
import ru.aza1rat.playlistmaker.db.data.model.TrackDbModel

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackDbModel)

    @Query("DELETE FROM track WHERE id = :id")
    suspend fun deleteTrack(id: Int)

    @Query("SELECT * FROM track ORDER BY added_at DESC")
    fun getTracksOrderedByAdding(): Flow<List<TrackDbModel>>

    @Query("SELECT COUNT(*) FROM track WHERE id = :id")
    suspend fun getTracksCountById(id: Int): Int

    @Deprecated("По требованию этот метод должен использоваться при поиске треков. " +
            "Используйте getTracksCountById на экране плеера чтобы убедиться, что трек действительно находится в избранном")
    @Query("SELECT id FROM track")
    suspend fun getTracksIds(): List<Int>
}