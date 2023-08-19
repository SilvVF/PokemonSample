package io.silv.pokemon.network.types.paginated


import com.google.gson.annotations.SerializedName

/**
 * This class contains the response from calling https://pokeapi.co/api/v2/pokemon?offset=10&limit=10.
 * It returns a list of urls to call the api with for the pokemon data
 *
 * @param count the last offset number will be equal to this.
 *
 * [PokeApiDocs](https://pokeapi.co/docs/v2#resource-listspagination-section)
 */
data class PokemonPaginatedResourceList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<Result>
) {
    data class Result(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )
}