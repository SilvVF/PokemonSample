package io.silv.pokemon.network

import com.google.gson.Gson
import io.silv.pokemon.network.types.paginated.PokemonPaginatedResourceList
import io.silv.pokemon.network.types.pokemon.Pokemon
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

val gson = Gson()

object PokemonApi {

    private const val pokemonApiV2Url = "https://pokeapi.co/api/v2/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(pokemonApiV2Url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val client: PokemonService by lazy {
        retrofit.create(PokemonService::class.java)
    }
}

interface PokemonService {

    @GET("pokemon")
    suspend fun getPokemonPagingData(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PokemonPaginatedResourceList>

    @GET("pokemon/{id}")
    suspend fun getPokemonData(
        @Path("id") id: Int
    ): Response<Pokemon>
}