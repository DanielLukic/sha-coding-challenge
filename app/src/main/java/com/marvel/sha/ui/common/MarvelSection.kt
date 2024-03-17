package com.marvel.sha.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun MarvelSection(
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Section(title, color)
    Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Column(modifier = Modifier.padding(8.dp), content = content)
    }
}

@Composable
internal fun MarvelSection(title: String, color: Color, text: String) =
    MarvelSection(title, color) { Text(text = text) }
