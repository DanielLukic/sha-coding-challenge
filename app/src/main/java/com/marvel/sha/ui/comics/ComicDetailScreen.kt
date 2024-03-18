package com.marvel.sha.ui.comics

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bx.util.onNotEmpty
import com.marvel.sha.data.string
import com.marvel.sha.ui.common.InlineImage
import com.marvel.sha.ui.common.MarvelSection
import com.marvel.sha.ui.common.PaletteDetailScreen
import com.marvel.sha.util.onNotBlank
import org.koin.androidx.compose.koinViewModel

@Composable @OptIn(ExperimentalLayoutApi::class)
internal fun ComicDetailScreen(
    modifier: Modifier = Modifier,
    model: ComicDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) = PaletteDetailScreen(
    modifier = modifier,
    detail = model.observe().collectAsState().value.valueOrNull(),
    title = { it.title },
    thumbnail = { it.thumbnail.url },
    onBackClick = onBackClick
) { detail ->
    LazyColumn {
        item {
            InlineImage(detail.thumbnail, modifier = Modifier.height(300.dp).padding(16.dp))
        }
        detail.description?.onNotBlank {
            item { MarvelSection("Description", it) }
        }
        detail.characters().onNotEmpty {
            item { MarvelSection("Characters", it.joinToString(", ") { it.name }) }
        }
        detail.creators().onNotEmpty {
            item { MarvelSection("Creators", it.joinToString(", ") { it.name }) }
        }
        detail.textObjects.forEach {
            item { MarvelSection(it.string("type"), it.string("text")) }
        }
        detail.urls.onNotEmpty {
            item {
                MarvelSection("Web Links") {
                    FlowRow {
                        it.forEach { Button(onClick = {}) { Text(it.type) } }
                    }
                }
            }
        }
        detail.images.forEach {
            if (it == detail.thumbnail) return@forEach
            item { MarvelSection("Extra Image") { InlineImage(it) } }
        }
    }
}
