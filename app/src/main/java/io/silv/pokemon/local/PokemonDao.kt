package io.silv.pokemon.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

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