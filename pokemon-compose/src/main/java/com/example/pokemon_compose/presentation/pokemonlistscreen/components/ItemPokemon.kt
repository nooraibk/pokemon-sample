package com.example.pokemon_compose.presentation.pokemonlistscreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import com.example.pokemon_compose.domain.model.Poke
import com.example.pokemon_compose.domain.model.PokemonImage
import org.koin.compose.koinInject

@Composable
fun ItemPokemon(
    pokemon: Poke?,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = koinInject()
) {
    val alpha = 1.0f
    val context = LocalContext.current
    val primaryType = pokemon?.types?.firstOrNull()?.name ?: ""
    val typeColor = getPokemonTypeColor(primaryType)
    ItemContainer(
        modifier = modifier,
        itemBackground = ComposeBackground.GradientBackground(
            brush = Brush.verticalGradient(
                colors = listOf(
                    typeColor.copy(alpha = 0.1f * alpha),
                    MaterialTheme.colorScheme.surface.copy(alpha = alpha)
                )
            )
        )
    ) {
        Text(
            text = pokemon?.name?: "Pokemon",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        when (val image = pokemon?.image) {
            is PokemonImage.Local,
            is PokemonImage.Remote -> {
                val data = when (image) {
                    is PokemonImage.Local -> image.file
                    is PokemonImage.Remote -> image.url
                    else -> null
                }

                AsyncImage(
                    model = Builder(context)
                        .data(data)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Pokemon ${pokemon.name}",
                    imageLoader = imageLoader,
                    modifier = Modifier.wrapContentSize(),
                    contentScale = ContentScale.Fit
                )
            }

            is PokemonImage.None, null -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyRow {
            items(pokemon?.types?:emptyList(), key = { it.slot }) { type ->
                Pill(type.name, getPokemonTypeColor(type.name))
            }
        }

        Row {
            Stat("Height", pokemon?.height.toString())
            Stat("Weight", pokemon?.weight.toString())
        }
    }
}