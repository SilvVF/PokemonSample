package io.silv.pokemon.network

import com.google.gson.Gson
import io.silv.pokemon.network.types.paginated.PokemonPaginatedResourceList
import io.silv.pokemon.network.types.pokemon.Pokemon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * used to make network requests to [pokeApiV2](https://pokeapi.co/docs/v2#info)
 *
 * @property getPokemonPagingData
 *
 * @property getPokemonData
 */
interface PokemonService {

    /**
     * call to get the urls of the pokemon for pagination
     */
    @GET("pokemon")
    suspend fun getPokemonPagingData(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PokemonPaginatedResourceList>

    /**
     * call to get the pokemon data
     */
    @GET("pokemon/{id}")
    suspend fun getPokemonData(
        @Path("id") id: Int
    ): Response<Pokemon>
}