package com.marvel.sha.ui.common

import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
internal fun <T : Any> LoadingState(items: LazyPagingItems<T>) = with(items.loadState) {
    when {
        refresh is LoadState.Loading -> IndeterminateProgress()
        refresh is LoadState.Error   -> ErrorCard(refresh) { items.retry() }
        append is LoadState.Loading  -> IndeterminateProgress()
        append is LoadState.Error    -> ErrorCard(append) { items.retry() }
    }
}
