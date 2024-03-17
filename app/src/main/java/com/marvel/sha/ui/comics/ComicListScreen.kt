package com.marvel.sha.ui.comics

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.ui.searchlist.SearchListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ComicListScreen(
    modifier: Modifier = Modifier,
    model: ComicListViewModel = koinViewModel(),
    onClick: (MarvelComic) -> Unit,
) = SearchListScreen(
    modifier = modifier,
    state = model.state,
    query = model.query().collectAsStateWithLifecycle(),
    items = model.snapshot.collectAsLazyPagingItems(),
    imageUrl = { it.thumbnail.url },
    caption = { it.title },
    badge = { it.extraImagesBadge },
    onChangeQuery = model::changeQuery,
    onClearQuery = model::changeQuery,
    onClick = onClick
)
