package io.silv.pokemon.repository

import io.silv.pokemon.local.PokemonResource
import io.silv.pokemon.network.types.pokemon.Pokemon
import io.silv.pokemon.ui.home.UiPokemon

/**
 * Transforms [Pokemon] to a [PokemonResource] that can be stored in [io.silv.pokemon.local.PokemonDatabase]
 */
fun Pokemon.toResource(): PokemonResource {
    return PokemonResource(
        id = this.id,
        name = this.name,
        imageUrl = this.sprites.frontDefault
    )
}

/**
 * Transform [PokemonResource] to [UiPokemon].
 * if any additional state is need it can be combined into a UiPokemon.
 * This allows the Ui to not have to rely on the database type. if any property is added
 * that is only relevant to the data layer the UI does not need to have this information.
 */
fun PokemonResource.toUiPokemon(): UiPokemon {
    return UiPokemon(
        name = this.name,
        imageUrl = this.imageUrl,
        resourceId = this.id
    )
}