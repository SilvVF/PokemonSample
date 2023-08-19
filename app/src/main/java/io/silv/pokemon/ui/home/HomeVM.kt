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

    // uses a unidirectional data flow
    // state is passed downwards and ui displays it
    // events like refresh and retry are passed upwards
    // https://developer.android.com/jetpack/compose/architecture
    val pokemonPagingData = pokemonRepository.pager.flow
        .map { pagingData ->
            pagingData.map { it.toUiPokemon() }
        }
        // ctrl f cachedIn here  https://android-developers.googleblog.com/2020/07/getting-on-same-page-with-paging-3.html
        .cachedIn(viewModelScope)


}

data class UiPokemon(
    val name: String,
    val imageUrl: String,
    val resourceId: Int
)

