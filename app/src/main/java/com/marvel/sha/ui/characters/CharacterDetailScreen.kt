package com.marvel.sha.ui.characters

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import bx.util.Optional
import bx.util.onNotEmpty
import com.marvel.sha.ui.ShaTopBar
import com.marvel.sha.ui.common.*
import com.marvel.sha.util.onNotBlank
import com.marvel.sha.util.sanitized
import org.koin.androidx.compose.koinViewModel

@Composable @OptIn(ExperimentalLayoutApi::class)
internal fun CharacterDetailScreen(
    modifier: Modifier = Modifier,
    model: CharacterDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val detail = model.observe().collectAsState().value.valueOrNull()
    val title = detail?.name?.sanitized() ?: ""

    val palette = remember { mutableStateOf<Optional<Palette>>(Optional.empty()) }
    detail?.thumbnail?.let { AsyncPalette(it, palette) }

    val sectionColor = palette.sectionColor()

    Scaffold(topBar = { ShaTopBar(title = title, onBackClick = onBackClick) }) { padding ->
        Surface(
            tonalElevation = 4.dp,
            modifier = modifier.padding(padding)
        ) {
            if (detail == null) {
                IndeterminateProgress()
            }
            else LazyColumn {
                item {
                    InlineImage(detail.thumbnail, modifier = Modifier.height(300.dp).padding(16.dp))
                }
                detail.description?.onNotBlank {
                    item { MarvelSection(sectionColor, "Description", it) }
                }
                detail.comics().onNotEmpty {
                    item { MarvelSection(sectionColor, "Comics", it.joinToString(", ") { it.name }) }
                }
                detail.stories().onNotEmpty {
                    item { MarvelSection(sectionColor, "Stories", it.joinToString(", ") { it.name }) }
                }
                detail.series().onNotEmpty {
                    item { MarvelSection(sectionColor, "Series", it.joinToString(", ") { it.name }) }
                }
                detail.urls.onNotEmpty {
                    item {
                        MarvelSection(sectionColor, "Web Links") {
                            FlowRow { it.forEach { Button(onClick = {}) { Text(it.type) } } }
                        }
                    }
                }
            }
        }
    }
}
