package com.example.pokemon_compose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pokemon_compose.data.remote.ASYNC_PAGE_SIZE
import com.example.pokemon_compose.data.remote.AsyncPokePagingSource
import com.example.pokemon_compose.data.remote.ImageDownloadService
import com.example.pokemon_compose.data.remote.PokemonApiService
import com.example.pokemon_compose.domain.model.Poke
import kotlinx.coroutines.flow.Flow

class PokemonRepository(
    private val apiService: PokemonApiService,
    private val imageDownloadService: ImageDownloadService
) {
    fun getPokemonPagingData(): Flow<PagingData<Poke>> {
        return Pager(
            config = PagingConfig(
                pageSize = ASYNC_PAGE_SIZE,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = ASYNC_PAGE_SIZE,
                maxSize = 200
            ),
            pagingSourceFactory = { AsyncPokePagingSource(apiService, imageDownloadService) }
        ).flow
    }
}