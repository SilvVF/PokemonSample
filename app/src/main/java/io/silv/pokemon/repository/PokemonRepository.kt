package io.silv.pokemon.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.silv.pokemon.local.PokeDb
import io.silv.pokemon.network.PokemonApi
import io.silv.pokemon.repository.paging.PokemonRemoteMediator


class PokemonPagingRepository {

    private val db by lazy { PokeDb.getInstance() }
    private val api = PokemonApi.client

    @OptIn(ExperimentalPagingApi::class)
    val pager = Pager(
        config = PagingConfig(
            pageSize = 30,
        ),
        remoteMediator = PokemonRemoteMediator(db, api),
        pagingSourceFactory = { db.pokemonDao().pagingData() }
    )
}