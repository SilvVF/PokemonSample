package io.silv.pokemon.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PokemonResource(
    @PrimaryKey val id: Int,

    val name: String,
    val imageUrl: String,
)
