package com.example.pokemon_compose.presentation.pokemonlistscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import com.example.pokemon_compose.presentation.pokemonlistscreen.components.PokemonCarousel
import com.example.pokemon_compose.presentation.viewmodel.PokemonListViewModel
import org.koin.androidx.compose.koinViewModel

class PokemonListScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: PokemonListViewModel = koinViewModel()
        val pokemonPagingItems = viewModel.pokemonPagingData.collectAsLazyPagingItems()

        Scaffold { paddingValues ->
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Companion.Center
            ) {
                PokemonCarousel(
                    pokemonPagingItems = pokemonPagingItems,
                    modifier = Modifier.Companion.fillMaxSize()
                )
            }
        }
    }
}