package com.example.pokemon_compose.di

import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.SvgDecoder
import com.example.pokemon_compose.BuildConfig
import com.example.pokemon_compose.data.remote.ImageDownloadService
import com.example.pokemon_compose.data.remote.PokemonApiService
import com.example.pokemon_compose.data.repository.PokemonRepository
import com.example.pokemon_compose.presentation.viewmodel.PokemonListViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single<ImageLoader> {
        ImageLoader.Builder(get())
            .components {
                add(SvgDecoder.Factory())
                add(GifDecoder.Factory())
            }
            .crossfade(true)
            .build()
    }

    single<Gson> {
        GsonBuilder()
            .setLenient()
            .create()
    }

    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<PokemonApiService> {
        get<Retrofit>().create(PokemonApiService::class.java)
    }

    single<ImageDownloadService> {
        ImageDownloadService(androidContext())
    }

    single { PokemonRepository(apiService = get(), imageDownloadService = get()) }

    viewModel { PokemonListViewModel(get()) }
}
