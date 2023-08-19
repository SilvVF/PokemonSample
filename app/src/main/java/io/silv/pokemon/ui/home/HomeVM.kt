package io.silv.pokemon.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import io.silv.pokemon.repository.PokemonPagingRepository
import io.silv.pokemon.repository.toUiPokemon
import kotlinx.coroutines.flow.map

class HomeVM: ViewModel() {

    private val pokemonRepository = PokemonPagingRepository()

    val pokemonPagingData = pokemonRepository.pager.flow
        .map { pagingData ->
            pagingData.map { it.toUiPokemon() }
        }
        .cachedIn(viewModelScope)
}

data class UiPokemon(
    val name: String,
    val imageUrl: String,
    val resourceId: Int
)

