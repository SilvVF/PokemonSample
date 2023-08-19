package io.silv.pokemon.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class RemoteKey(
    @PrimaryKey val id: Int,
    val offset: Int,
)

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM remotekey WHERE id = :id LIMIT 1")
    suspend fun getKeyById(id: Int): RemoteKey?

    @Query("DELETE FROM remotekey")
    suspend fun clear()
}