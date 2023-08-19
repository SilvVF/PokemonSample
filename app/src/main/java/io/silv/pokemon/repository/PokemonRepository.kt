package io.silv.pokemon.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.silv.pokemon.local.PokemonDatabase
import io.silv.pokemon.network.PokemonService
import io.silv.pokemon.repository.paging.PokemonRemoteMediator


class PokemonRepository(
    private val db: PokemonDatabase,
    api: PokemonService
) {

    @OptIn(ExperimentalPagingApi::class)
    val pager = Pager(
        config = PagingConfig(
            pageSize = 30,
        ),
        remoteMediator = PokemonRemoteMediator(db, api),
        pagingSourceFactory = { db.pokemonDao().pagingData() }
    )
}