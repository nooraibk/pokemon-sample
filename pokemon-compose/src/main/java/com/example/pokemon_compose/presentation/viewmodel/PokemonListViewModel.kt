package com.example.pokemon_compose.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemon_compose.data.repository.PokemonRepository
import com.example.pokemon_compose.domain.model.Poke
import kotlinx.coroutines.flow.Flow

class PokemonListViewModel(
    pokemonRepository: PokemonRepository
) : ViewModel() {
    val pokemonPagingData: Flow<PagingData<Poke>> = pokemonRepository
        .getPokemonPagingData()
        .cachedIn(viewModelScope)
}