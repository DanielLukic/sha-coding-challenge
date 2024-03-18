package com.marvel.sha.ui.creators

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.marvel.sha.domain.MarvelCreator
import com.marvel.sha.ui.searchlist.SearchListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CreatorListScreen(
    modifier: Modifier = Modifier,
    model: CreatorListViewModel = koinViewModel(),
    onClick: (MarvelCreator) -> Unit,
) = SearchListScreen(
    modifier = modifier,
    state = model.state,
    query = model.query,
    items = model.snapshot.collectAsLazyPagingItems(),
    imageUrl = { it.thumbnail.url },
    caption = { it.fullName },
    onChangeQuery = model::changeQuery,
    onClearQuery = model::changeQuery,
    onClick = onClick,
)
