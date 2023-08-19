package io.silv.pokemon.repository.paging

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

/**
 * [RemoteMediator] used to make network requests based on
 * the current [PagingState] and sync the data with [PokemonDatabase].
 * [RemoteKey] is used to keep track of the prev offset
 * [PokemonResource] holds the current paging data.
 * - [paging 3 docs](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db)
 */
@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val db: PokemonDatabase,
    private val api: PokemonService
): RemoteMediator<Int, PokemonResource>() {

    private val remoteKeysDao = db.remoteKeysDao()
    private val pokemonDao = db.pokemonDao()

    // get offset of the lastItem in the list of paging items currently displayed
    private suspend fun getPrevOffset(lastItem: PokemonResource?): Int? {
        if (lastItem == null) { return null }
        return remoteKeysDao.getKeyById(lastItem.id)?.offset
    }

    private fun getIdFromUrl(url: String): Int {
        return url.removeSuffix("/")
            .takeLastWhile { it.isDigit() }
            .toInt()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonResource>
    ): MediatorResult {
        return suspendRunCatching {
            // find the offset to use when making the call to the api
            val offset = when (loadType) {
                // refresh is the initial call
                LoadType.REFRESH -> 0
                // don't need to worry about this it should never be called as
                // no max page size is specified in the config in PokemonRepository
                LoadType.PREPEND -> return@suspendRunCatching MediatorResult.Success(endOfPaginationReached = true)
                // calculate the offset for the request using the last items offset
                LoadType.APPEND -> {
                    getPrevOffset(state.lastItemOrNull())?.let {
                        it + state.config.pageSize
                    } ?: 0
                }
            }
            // gets the list of resource urls
            val pokemonPagingResources = api.getPokemonPagingData(
                offset = offset,
                limit = state.config.pageSize
            )
                .body()!!
            // pmap makes each call in its own coroutine and returns the list of pokemon data
            val response = pokemonPagingResources.results.pmap {
                api.getPokemonData(
                    id = getIdFromUrl(it.url)
                )
                    .body()!!
            }
            // using a with transaction block makes it all or nothing
            // if one of the calls fail they will all be cancelled
            //ex. call to upsert fails all the prev data will not be deleted
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