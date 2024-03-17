package com.marvel.sha.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun ErrorCard(it: Any?, retry: () -> Unit = {}) = Card(
    modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 4.dp)
        .fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
) {
    Text(
        modifier = Modifier
            .clickable { retry() }
            .fillMaxWidth()
            .padding(8.dp),
        text = it.toString(),
        color = Color.Yellow,
        fontWeight = FontWeight.Bold,
    )
}
