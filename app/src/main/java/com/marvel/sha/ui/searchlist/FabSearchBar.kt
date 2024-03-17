package com.marvel.sha.ui.searchlist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun FabSearchBar(
    modifier: Modifier = Modifier,
    extendSearch: MutableState<Boolean> = remember { mutableStateOf(false) },
    query: State<String>,
    onChangeQuery: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    if (extendSearch.value) Row(modifier) {
        FloatingSearchField(query = query, onChangeQuery = onChangeQuery, onClearQuery = onClearQuery)
        FloatingActionButton(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = { extendSearch.value = false }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Close search"
            )
        }
    }
    else FloatingActionButton(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { extendSearch.value = true }
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Open search"
        )
    }
}
