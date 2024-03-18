package com.marvel.sha.ui.creators

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
import com.marvel.sha.ui.common.InlineImage
import com.marvel.sha.ui.common.MarvelSection
import com.marvel.sha.ui.common.PaletteDetailScreen
import org.koin.androidx.compose.koinViewModel

@Composable @OptIn(ExperimentalLayoutApi::class)
internal fun CreatorDetailScreen(
    modifier: Modifier = Modifier,
    model: CreatorDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) = PaletteDetailScreen(
    modifier = modifier,
    detail = model.observe().collectAsState().value.valueOrNull(),
    title = { it.fullName },
    thumbnail = { it.thumbnail.url },
    onBackClick = onBackClick
) { detail ->
    LazyColumn {
        item {
            InlineImage(detail.thumbnail, modifier = Modifier.height(300.dp).padding(16.dp))
        }
        detail.comics().onNotEmpty {
            item { MarvelSection("Comics", it.joinToString(", ") { it.name }) }
        }
        detail.stories().onNotEmpty {
            item { MarvelSection("Stories", it.joinToString(", ") { it.name }) }
        }
        detail.series().onNotEmpty {
            item { MarvelSection("Series", it.joinToString(", ") { it.name }) }
        }
        detail.urls.onNotEmpty {
            item {
                MarvelSection("Web Links") {
                    FlowRow { it.forEach { Button(onClick = {}) { Text(it.type) } } }
                }
            }
        }
    }
}
