package com.marvel.sha.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette

@Composable
internal fun Palette.MarvelSection(
    title: String,
    color: Color = sectionColor(),
    cardColor: Color = cardColor(),
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Section(title, color)
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(8.dp), content = content)
    }
}

@Composable
internal fun Palette.MarvelSection(
    title: String,
    text: String,
    color: Color = sectionColor(),
    cardColor: Color = cardColor(),
) = MarvelSection(title, color, cardColor) { Text(text = text) }

@Composable
internal fun MarvelSection(
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    cardColor: Color = MaterialTheme.colorScheme.surfaceBright,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Section(title, color)
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(8.dp), content = content)
    }
}

@Composable
internal fun MarvelSection(
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    cardColor: Color = MaterialTheme.colorScheme.surfaceBright,
    text: String
) = MarvelSection(title, color, cardColor) { Text(text = text) }
