package com.marvel.sha.ui.searchlist

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import bx.logging.Log
import com.marvel.sha.ui.common.LoadingState

@Composable @OptIn(ExperimentalMaterial3Api::class)
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
) = SearchBar(
    query = query.value,
    onQueryChange = { onChangeQuery(it) },
    onSearch = {},
    placeholder = {
        Text(text = "Filter by name")
    },
    leadingIcon = {
        Icon(
            imageVector = Icons.Default.Search,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    },
    trailingIcon = {
        if (query.value.isNotEmpty()) IconButton(onClick = onClearQuery) {
            Icon(
                imageVector = Icons.Default.Close,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "Clear search"
            )
        }
    },
    active = true,
    onActiveChange = {
        Log.warn { it }
        Log.warn { it }
        Log.warn { it }
    },
) {
    if (items.itemCount == 0) {
        LoadingState(items)
    }
    else {
        LazyVerticalGrid(
            modifier = modifier.imePadding(),
            columns = GridCells.Adaptive(160.dp),
            state = state
        ) {
            items(items.itemCount) { index ->
                val it = items[index] ?: return@items
                SearchListItem(it, badge(it), onClick, imageUrl, caption)
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                LoadingState(items)
            }
        }
    }
}
