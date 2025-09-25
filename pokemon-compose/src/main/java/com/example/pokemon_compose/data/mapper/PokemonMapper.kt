package com.example.pokemon_compose.data.mapper

import com.example.pokemon_compose.data.remote.models.PokemonDto
import com.example.pokemon_compose.data.remote.models.PokemonSpritesDto
import com.example.pokemon_compose.data.remote.models.PokemonTypeDto
import com.example.pokemon_compose.domain.model.*

fun PokemonDto.toPokeDomainModel(image : PokemonImage): Poke {
    return Poke(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        height = height,
        weight = weight,
        types = types.map { it.toPokemonType() },
        image = image,
    )
}

fun PokemonTypeDto.toPokemonType(): PokemonType {
    return PokemonType(
        slot = slot,
        name = type.name.replaceFirstChar { it.uppercase() }
    )
}

fun PokemonSpritesDto.getBestImageUrl(): String? {
    return other?.officialArtwork?.frontDefault ?: other?.dreamWorld?.frontDefault ?: frontDefault
}