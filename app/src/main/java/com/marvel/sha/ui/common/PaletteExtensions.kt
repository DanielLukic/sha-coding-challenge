package com.marvel.sha.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import bx.util.Optional

@Composable
internal fun MutableState<Optional<Palette>>.sectionColor(): Color =
    value.valueOrNull()?.lightVibrantSwatch?.rgb?.let { Color(it) } ?: MaterialTheme.colorScheme.primary
