package com.marvel.sha.ui.searchlist

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.marvel.sha.ui.common.LoadingState

@Composable @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun <T : Any> SearchListScreen(
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    query: State<String>,
    items: LazyPagingItems<T>,
    imageUrl: (T) -> String,
    caption: (T) -> String,
    badge: (T) -> Any? = { null },
    onChangeQuery: (String) -> Unit,
    onClearQuery: () -> Unit,
    onClick: (T) -> Unit,
) = Box {
    val activity = LocalContext.current as Activity
    val portrait = calculateWindowSizeClass(activity).heightSizeClass != WindowHeightSizeClass.Compact
    if (items.itemCount == 0) {
        LoadingState(items)
    }
    else {
        LazyVerticalGrid(
            modifier = modifier.imePadding(),
            columns = GridCells.Adaptive(160.dp),
            state = state
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(8.dp))
            }
            items(items.itemCount) { index ->
                val it = items[index] ?: return@items
                SearchListItem(it, badge(it), onClick, imageUrl, caption)
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingState(items)
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(if (portrait) 72.dp else 8.dp))
            }
        }
    }
    if (portrait) FloatingSearchField(
        modifier = Modifier.align(Alignment.BottomStart),
        query = query,
        onChangeQuery = onChangeQuery,
        onClearQuery = onClearQuery
    )
    else FabSearchBar(
        modifier = Modifier.align(Alignment.BottomEnd),
        query = query,
        onChangeQuery = onChangeQuery,
        onClearQuery = onClearQuery
    )
}
