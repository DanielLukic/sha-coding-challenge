package com.marvel.sha.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun Section(it: String, color: Color = Color.Yellow) = Text(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp),
    style = MaterialTheme.typography.titleMedium,
    color = color,
    textAlign = TextAlign.Center,
    text = it
)
