package com.marvel.sha.ui.creators

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
import com.marvel.sha.util.sanitized
import org.koin.androidx.compose.koinViewModel

@Composable @OptIn(ExperimentalLayoutApi::class)
internal fun CreatorDetailScreen(
    modifier: Modifier = Modifier,
    model: CreatorDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val detail = model.observe().collectAsState().value.valueOrNull()
    val title = detail?.fullName?.sanitized() ?: ""

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
                detail.comics().onNotEmpty {
                    item { MarvelSection("Comics", sectionColor, it.joinToString(", ") { it.name }) }
                }
                detail.stories().onNotEmpty {
                    item { MarvelSection("Stories", sectionColor, it.joinToString(", ") { it.name }) }
                }
                detail.series().onNotEmpty {
                    item { MarvelSection("Series", sectionColor, it.joinToString(", ") { it.name }) }
                }
                detail.urls.onNotEmpty {
                    item {
                        MarvelSection("Web Links", sectionColor) {
                            FlowRow { it.forEach { Button(onClick = {}) { Text(it.type) } } }
                        }
                    }
                }
            }
        }
    }
}
