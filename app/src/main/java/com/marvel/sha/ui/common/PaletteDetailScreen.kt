package com.marvel.sha.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import bx.util.Optional
import com.marvel.sha.ui.ShaTopBar
import com.marvel.sha.ui.comics.lighten
import com.marvel.sha.util.sanitized

@Composable
internal fun <T : Any> PaletteDetailScreen(
    modifier: Modifier = Modifier,
    detail: T?,
    title: (T) -> String,
    thumbnail: (T) -> String?,
    onBackClick: () -> Unit,
    data: @Composable Palette.(T) -> Unit,
) {
    val paletteState = remember { mutableStateOf<Optional<Palette>>(Optional.empty()) }
    detail?.let(thumbnail)?.let { AsyncPalette(it, paletteState) }

    val palette = paletteState.value.valueOrNull()
    val surfaceColor = palette.surfaceColor()
    val elevatedColor = surfaceColor.lighten()

    Scaffold(
        topBar = {
            ShaTopBar(
                color = elevatedColor,
                title = detail?.let(title)?.sanitized() ?: "",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Surface(
            tonalElevation = 4.dp,
            color = surfaceColor,
            modifier = modifier.padding(padding)
        ) {
            if (detail == null) IndeterminateProgress() else palette?.data(detail)
        }
    }
}
