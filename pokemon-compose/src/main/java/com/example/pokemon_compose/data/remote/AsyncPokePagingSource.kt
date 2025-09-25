package com.example.pokemon_compose.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokemon_compose.data.mapper.getBestImageUrl
import com.example.pokemon_compose.data.mapper.toPokeDomainModel
import com.example.pokemon_compose.domain.model.Poke
import com.example.pokemon_compose.domain.model.PokemonImage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okio.IOException
import java.io.File

const val ASYNC_STARTING_PAGE_INDEX = 1
const val ASYNC_PAGE_SIZE = 20

class AsyncPokePagingSource(
    private val api: PokemonApiService,
    private val imageDownloadService: ImageDownloadService
) : PagingSource<Int, Poke>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Poke> {
        return try {
            val nextPageNumber = params.key ?: ASYNC_STARTING_PAGE_INDEX
            val dataList = loadPokemonAsynchronously(params.loadSize, nextPageNumber)
            val nextKey = if (dataList.size < params.loadSize) null else nextPageNumber + 1
            val previousKey =
                if (nextPageNumber == ASYNC_STARTING_PAGE_INDEX) null else nextPageNumber - 1

            if (dataList.isEmpty()) {
                LoadResult.Error(Exception("No data found"))
            } else {
                LoadResult.Page(dataList, previousKey, nextKey)
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Poke>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private suspend fun loadPokemonAsynchronously(
        pageSize: Int,
        pageNumber: Int
    ): List<Poke> = coroutineScope {
        runCatching {
            val offset = (pageNumber - 1) * pageSize

            val pokemonListResponse = api.getPokemonList(
                limit = pageSize,
                offset = offset
            )

            if (!pokemonListResponse.isSuccessful) {
                return@coroutineScope emptyList()
            }

            val pokemonListItems = pokemonListResponse.body()?.results ?: emptyList()

            val pokemonJobs = pokemonListItems.map { listItem ->
                async {
                    runCatching {
                        val pokemonId = listItem.url.split("/").dropLast(1).last().toInt()

                        val detailResponse = api.getPokemonDetail(pokemonId)
                        if (detailResponse.isSuccessful) {
                            val pokemonDto = detailResponse.body()
                            if (pokemonDto != null) {
                                val pokemonImage: PokemonImage = PokemonImage.None
                                val pokemon = pokemonDto.toPokeDomainModel(pokemonImage)

                                val imageJob = async {
                                    runCatching {
                                        if (imageDownloadService.isImageCached(pokemonId)) {
                                            return@async PokemonImage.Local(
                                                File(imageDownloadService.getImageFile(pokemonId).absolutePath)
                                            )
                                        }

                                        val imageUrl = pokemonDto.sprites.getBestImageUrl()
                                        if (imageUrl != null) {
                                            val resolvedFile = imageDownloadService.downloadImage(imageUrl, pokemonId)
                                            return@async if (resolvedFile != null)
                                                PokemonImage.Local(resolvedFile)
                                            else
                                                PokemonImage.Remote(imageUrl)
                                        } else {
                                            return@async PokemonImage.None
                                        }
                                    }.getOrElse { e ->
                                        e.printStackTrace()
                                        PokemonImage.None
                                    }
                                }

                                val downloadedImage: PokemonImage = runCatching {
                                    imageJob.await()
                                }.getOrElse { e ->
                                    e.printStackTrace()
                                    pokemonImage
                                }

                                return@async pokemon.copy(image = downloadedImage)
                            }
                        }
                        null
                    }.getOrElse { e ->
                        e.printStackTrace()
                        null
                    }
                }
            }

            val results = pokemonJobs.awaitAll().filterNotNull()
            return@coroutineScope results
        }.getOrElse { e ->
            e.printStackTrace()
            emptyList()
        }

    }
}