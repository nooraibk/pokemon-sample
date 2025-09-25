package com.example.pokemon_compose.presentation.pokemonlistscreen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.example.pokemon_compose.domain.model.Poke
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonCarousel(
    pokemonPagingItems: LazyPagingItems<Poke>,
    modifier: Modifier = Modifier
) {
    val refreshLoadState = pokemonPagingItems.loadState.refresh
    val appendLoadState = pokemonPagingItems.loadState.append
    if (refreshLoadState is LoadState.Loading && pokemonPagingItems.itemCount == 0) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemLoading()
        }
        return
    }

    if (refreshLoadState is LoadState.Error) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemError(
                error = refreshLoadState.error.message ?: "Unknown error occurred",
                onRetry = {
                    pokemonPagingItems.retry()
                }
            )
        }
        return
    }

    if (pokemonPagingItems.itemCount == 0 && refreshLoadState is LoadState.NotLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemError(
                error = "No Pokemon available",
                onRetry = {
                    pokemonPagingItems.retry()
                }
            )
        }
        return
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val dpCacheWindow = LazyLayoutCacheWindow(ahead = 450.dp, behind = 450.dp)

    val lazyListState = rememberLazyListState(cacheWindow = dpCacheWindow)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { newIndex ->
                if (newIndex != selectedIndex && newIndex < pokemonPagingItems.itemCount) {
                    selectedIndex = newIndex
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val totalCount = if (appendLoadState.endOfPaginationReached) {
            pokemonPagingItems.itemCount
        } else {
            pokemonPagingItems.itemCount + 1
        }

        Text(
            text = "${selectedIndex + 1} / ${if (appendLoadState.endOfPaginationReached) totalCount else "${totalCount}+"}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = screenWidth / 2 - 140.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(
                count = pokemonPagingItems.itemCount,
                key = pokemonPagingItems.itemKey { it.id }
            ) { index ->
                val pokemon = pokemonPagingItems[index]

                if (pokemon != null) {

                    ItemPokemon(
                        pokemon = pokemon
                    )

//                    PokemonCard(
//                        pokemon = pokemon,
//                        isSelected = isSelected,
//                        modifier = Modifier
//                            .width(280.dp)
//                            .scale(scale)
//                            .clip(MaterialTheme.shapes.extraLarge),
//                        alpha = alpha
//                    )
                } else {
                    Card(
                        modifier = Modifier
                            .width(280.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            if (appendLoadState is LoadState.Loading) {
                item(key = "loading_more") {
                    /*LoadingCard*/ItemLoading()
                }
            }

            if (appendLoadState is LoadState.Error) {
                item(key = "loading_error") {
                    ItemError(
                        error = appendLoadState.error.message ?: "Please Try Again",
                        onRetry = { pokemonPagingItems.retry() }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun getPokemonTypeColor(typeName: String): Color {
    return when (typeName.lowercase()) {
        "normal" -> Color(0xFFA8A878)
        "fire" -> Color(0xFFF08030)
        "water" -> Color(0xFF6890F0)
        "electric" -> Color(0xFFF8D030)
        "grass" -> Color(0xFF78C850)
        "ice" -> Color(0xFF98D8D8)
        "fighting" -> Color(0xFFC03028)
        "poison" -> Color(0xFFA040A0)
        "ground" -> Color(0xFFE0C068)
        "flying" -> Color(0xFFA890F0)
        "psychic" -> Color(0xFFF85888)
        "bug" -> Color(0xFFA8B820)
        "rock" -> Color(0xFFB8A038)
        "ghost" -> Color(0xFF705898)
        "dragon" -> Color(0xFF7038F8)
        "dark" -> Color(0xFF705848)
        "steel" -> Color(0xFFB8B8D0)
        "fairy" -> Color(0xFFEE99AC)
        else -> Color(0xFF68A090)
    }
}
