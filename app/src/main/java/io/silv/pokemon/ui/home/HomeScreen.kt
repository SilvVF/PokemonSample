package io.silv.pokemon.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.silv.DependencyGraph
import io.silv.pokemon.ui.theme.Pastel

class HomeVMFactory: ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeVM(
            pokemonRepository = DependencyGraph.pokemonRepository
        ) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val viewModel = viewModel<HomeVM>(factory = HomeVMFactory())
    // collectAsLazyPagingItems is provided in paging 3 library
    val pokemonPagingData = viewModel.pokemonPagingData.collectAsLazyPagingItems()

    Scaffold { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PokemonList(
                modifier = Modifier.fillMaxSize(),
                pokemon = pokemonPagingData
            )
        }
    }
}

@Composable
fun PokemonList(
    modifier: Modifier,
    pokemon: LazyPagingItems<UiPokemon>
) {
    if (pokemon.loadState.refresh is LoadState.Loading) {
        Box(modifier = modifier) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        Column(
            modifier = modifier
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                columns = GridCells.Fixed(2)
            ) {
                items(
                    count = pokemon.itemCount,
                    key = pokemon.itemKey(),
                    contentType = pokemon.itemContentType()
                ) { idx ->
                    pokemon[idx]?.let {
                        PokemonListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            pokemon = it
                        )
                    }
                }
            }
            if (pokemon.loadState.append is LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            if (pokemon.loadState.refresh is LoadState.Error || pokemon.loadState.append is LoadState.Error) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Button(onClick = { pokemon.retry() }) {
                        Text(text = "retry loading pokemon")
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonListItem(
    modifier: Modifier = Modifier,
    pokemon: UiPokemon,
) {
    val context = LocalContext.current
    val placeHolder = remember {
        Pastel.getColorLight()
    }
    Column(
        modifier = modifier.padding(22.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(pokemon.imageUrl)
                .crossfade(true)
                .placeholder(placeHolder)
                .fallback(placeHolder)
                .error(placeHolder)
                .build(),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
        )
        Text(pokemon.name)
    }
}