package com.marvel.sha.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.marvel.sha.ui.comics.lighten

@Composable
internal fun Palette?.cardColor(): Color =
    surfaceColor().lighten()

@Composable
internal fun Palette?.surfaceColor(): Color =
    this?.darkMutedSwatch?.rgb?.let { Color(it) } ?: MaterialTheme.colorScheme.surface

@Composable
internal fun Palette?.sectionColor(): Color =
    this?.lightVibrantSwatch?.rgb?.let { Color(it) } ?: MaterialTheme.colorScheme.primary
