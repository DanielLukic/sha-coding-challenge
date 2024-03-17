package com.marvel.sha.ui.characters

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.ui.searchlist.SearchListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CharacterListScreen(
    modifier: Modifier = Modifier,
    model: CharacterListViewModel = koinViewModel(),
    state: LazyGridState = model.state,
    onClick: (MarvelCharacter) -> Unit,
) = SearchListScreen(
    modifier = modifier,
    state = state,
    query = model.query().collectAsStateWithLifecycle(),
    items = model.snapshot.collectAsLazyPagingItems(),
    imageUrl = { it.thumbnail.url },
    caption = { it.name },
    onChangeQuery = model::changeQuery,
    onClearQuery = model::changeQuery,
    onClick = onClick,
)
