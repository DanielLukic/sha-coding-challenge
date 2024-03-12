package com.marvel.sha.ui.creators

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import bx.logging.Log
import coil.compose.AsyncImage
import com.marvel.sha.domain.MarvelCreator
import com.marvel.sha.ui.ErrorCard
import com.marvel.sha.ui.IndeterminateProgress
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun CreatorListScreen(
    modifier: Modifier = Modifier,
    model: CreatorListViewModel = koinViewModel(),
    onClick: (MarvelCreator) -> Unit,
) {
    val items = model.observe().collectAsLazyPagingItems()
    LazyColumn(modifier = modifier.imePadding()) {
        item { Spacer(modifier = Modifier.height(8.dp)) }
        items(items.itemCount) { index ->
            items[index]?.let {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(it) },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AsyncImage(
                            modifier = Modifier.size(48.dp),
                            model = it.thumbnail.url,
                            contentDescription = "Thumbnail",
                            onState = { state -> Log.verbose("${it.fullName}:${state.javaClass.simpleName}") }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(modifier = Modifier.fillMaxWidth(), text = it.fullName)
                    }
                }
            }
        }
        with(items.loadState) {
            when {
                refresh is LoadState.Loading -> item { IndeterminateProgress() }
                refresh is LoadState.Error   -> item { ErrorCard(refresh) }
                append is LoadState.Loading  -> item { IndeterminateProgress() }
                append is LoadState.Error    -> item { ErrorCard(append) }
            }
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}
