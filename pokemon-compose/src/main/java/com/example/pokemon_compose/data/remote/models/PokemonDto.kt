package com.example.pokemon_compose.data.remote.models

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("types")
    val types: List<PokemonTypeDto>,
    @SerializedName("sprites")
    val sprites: PokemonSpritesDto
)

data class PokemonTypeDto(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: TypeInfoDto
)

data class TypeInfoDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class PokemonSpritesDto(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?,
    @SerializedName("other")
    val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    @SerializedName("dream_world")
    val dreamWorld: DreamWorldSpritesDto?,
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtworkSpritesDto?
)

data class DreamWorldSpritesDto(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class OfficialArtworkSpritesDto(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class PokemonListResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<PokemonListItemDto>
)

data class PokemonListItemDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)
