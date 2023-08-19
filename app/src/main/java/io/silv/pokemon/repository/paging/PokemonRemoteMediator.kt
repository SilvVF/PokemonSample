package io.silv.pokemon.repository.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import io.silv.pokemon.local.PokemonDatabase
import io.silv.pokemon.local.PokemonResource
import io.silv.pokemon.local.RemoteKey
import io.silv.pokemon.network.PokemonService
import io.silv.pokemon.pmap
import io.silv.pokemon.repository.toResource
import io.silv.pokemon.suspendRunCatching

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val db: PokemonDatabase,
    private val api: PokemonService
): RemoteMediator<Int, PokemonResource>() {

    private val remoteKeysDao = db.remoteKeysDao()
    private val pokemonDao = db.pokemonDao()

    private suspend fun getPrevOffset(lastItem: PokemonResource?): Int? {
        if (lastItem == null) { return null }
        return remoteKeysDao.getKeyById(lastItem.id)?.offset
    }

    private fun getIdFromUrl(url: String): Int {
        Log.d("url", url)
        return url.removeSuffix("/")
            .takeLastWhile { it.isDigit() }
            .toInt()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonResource>
    ): MediatorResult {
        return suspendRunCatching {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return@suspendRunCatching MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    getPrevOffset(state.lastItemOrNull())?.let {
                        it + state.config.pageSize
                    } ?: 0
                }
            }
            val pokemonPagingResources = api.getPokemonPagingData(
                offset = offset,
                limit = state.config.pageSize
            )
                .body()!!
            val response = pokemonPagingResources.results.pmap {
                api.getPokemonData(
                    id = getIdFromUrl(it.url)
                )
                    .body()!!
            }

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    remoteKeysDao.clear()
                    pokemonDao.clear()
                }
                pokemonDao.upsertAll(
                    resources = response.map { it.toResource() }
                )
                remoteKeysDao.upsertAll(response.map { RemoteKey(it.id, offset) })
            }
            MediatorResult.Success(
                endOfPaginationReached = offset >= pokemonPagingResources.count
            )
        }
            .getOrElse {
                MediatorResult.Error(it)
            }
    }
}