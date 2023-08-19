package io.silv.pokemon.repository

import io.silv.pokemon.local.PokemonResource
import io.silv.pokemon.network.types.pokemon.Pokemon
import io.silv.pokemon.ui.home.UiPokemon

fun Pokemon.toResource(): PokemonResource {
    return PokemonResource(
        id = this.id,
        name = this.name,
        imageUrl = this.sprites.frontDefault
    )
}

fun PokemonResource.toUiPokemon(): UiPokemon {
    return UiPokemon(
        name = this.name,
        imageUrl = this.imageUrl,
        resourceId = this.id
    )
}