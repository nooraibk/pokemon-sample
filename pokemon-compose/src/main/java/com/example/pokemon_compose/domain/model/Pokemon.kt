package com.example.pokemon_compose.domain.model

import java.io.File

data class Poke(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonType>,
    val image : PokemonImage
){
    companion object {
        val pikachu = Poke(
            id = 25,
            name = "Pikachu",
            height = 4,
            weight = 60,
            types = listOf(
                PokemonType(slot = 1, name = "Electric")
            ),
            image = PokemonImage.Remote(
                url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png"
            )
        )
    }
}

data class PokemonType(
    val slot: Int,
    val name: String
)

sealed class PokemonImage {
    data class Remote(val url: String) : PokemonImage()
    data class Local(val file : File) : PokemonImage()
    object None : PokemonImage()
}

