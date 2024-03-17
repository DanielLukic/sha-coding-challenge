package com.marvel.sha.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.marvel.sha.domain.MarvelImage

@Composable
internal fun InlineImage(it: MarvelImage, modifier: Modifier = Modifier) = AsyncImage(
    modifier = modifier.fillMaxWidth(),
    model = it.url,
    contentDescription = "Image"
)
