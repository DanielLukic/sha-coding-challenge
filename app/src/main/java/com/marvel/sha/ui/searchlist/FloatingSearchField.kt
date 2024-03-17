package com.marvel.sha.ui.searchlist

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
internal fun FloatingSearchField(
    modifier: Modifier = Modifier,
    query: State<String>,
    onChangeQuery: (String) -> Unit,
    onClearQuery: () -> Unit
) = Surface(
    modifier = modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clip(MaterialTheme.shapes.medium),
    color = TextFieldDefaults.colors().focusedContainerColor
) {
    Row {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            modifier = Modifier.align(Alignment.CenterVertically).size(32.dp),
            imageVector = Icons.Default.Search,
            contentDescription = "Search"
        )
        SearchField(query, onChangeQuery, onClearQuery)
    }
}
