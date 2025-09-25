package com.example.pokemon_compose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.example.pokemon_compose.presentation.pokemonlistscreen.PokemonListScreen
import com.example.pokemon_compose.presentation.viewmodel.PokemonListViewModel
import com.example.pokemon_compose.ui.theme.SampleProjectsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewmodel: PokemonListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel
        setContent {
            SampleProjectsTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigator(screen = PokemonListScreen())
//                    Navigator(screen = PokeHomeScreen())
                }
            }
        }
    }
}