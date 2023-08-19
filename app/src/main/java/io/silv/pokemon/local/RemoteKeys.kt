package io.silv.pokemon.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query


/**
 * @property id id of the associated pokemon resource in [PokemonResource] table.
 * @property offset offset that was used when making the request using [io.silv.pokemon.network.PokemonService].
 *
 * Creating a separate table to hold the offset for pagination can be used
 * to avoid attaching the data to the entity itself. The other option would be to
 * attach an offset column to the [PokemonResource] itself.
 * Enforces separation of concerns docs show this method.
 *
 * - [paging 3 docs](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db)
 */
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