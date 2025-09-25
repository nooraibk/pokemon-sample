package com.example.pokemon_compose.presentation.pokemonlistscreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ItemError(
    modifier: Modifier = Modifier,
    error: String,
    onRetry: () -> Unit
) {
    ItemContainer(
        modifier = modifier,
        itemBackground = ComposeBackground.SolidBackground(color = Color.White)
    ) {
        Text(
            text = "Error Loading Pokemon",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Text(
            text = error,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.surfaceVariant,
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .clickable { onRetry() }
                .padding(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = "Try Again",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onError
            )
        }
    }
}