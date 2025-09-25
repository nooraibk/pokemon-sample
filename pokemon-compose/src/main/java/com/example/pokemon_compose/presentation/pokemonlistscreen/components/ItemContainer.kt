package com.example.pokemon_compose.presentation.pokemonlistscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ItemContainer(
    modifier: Modifier = Modifier,
    itemBackground: ComposeBackground,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .width(280.dp)
            .height(350.dp)
//            .clip(RoundedCornerShape(16.dp)) // will clip the entire modifier sequence
        ,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 1f)
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                /*.then(
                    when (itemBackground) {
                        is ComposeBackground.SolidBackground -> Modifier.background(
                            itemBackground.color
                        )
                        is ComposeBackground.GradientBackground -> Modifier.background(
                            itemBackground.brush
                        )
                    }
                )*/
                .customBackground(itemBackground)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }

    }
}

sealed interface ComposeBackground {
    data class SolidBackground(val color: Color) : ComposeBackground
    data class GradientBackground(val brush: Brush) : ComposeBackground
}

fun Modifier.customBackground(bg: ComposeBackground): Modifier =
    when (bg) {
        is ComposeBackground.SolidBackground -> this.background(bg.color)
        is ComposeBackground.GradientBackground -> this.background(bg.brush)
    }