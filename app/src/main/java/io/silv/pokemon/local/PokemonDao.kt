package io.silv.pokemon.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * [Dao] used to update pokemon resources for the paginated list.
 *
 * @property pagingData [PagingSource] used in [io.silv.pokemon.repository.PokemonPagingRepository]
 * to create the paging source for the paging source factory (see docs below).
 * @property clear [Query] deletes all resources in the [PokemonResource] table.
 * @property upsertAll [Insert] inserts all [PokemonResource]'s and replaces on conflict.
 * @property update [Update] update [PokemonResource] resource.
 *
 * - [paging 3 docs](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db)
 */
@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pokemonResource: PokemonResource)

    @Update
    suspend fun update(pokemonResource: PokemonResource)

    @Delete
    suspend fun delete(pokemonResource: PokemonResource)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(resources: List<PokemonResource>)

    @Query("DELETE FROM pokemonresource")
    suspend fun clear()

    @Query("SELECT * FROM pokemonresource")
    fun pagingData(): PagingSource<Int, PokemonResource>
}