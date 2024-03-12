package com.marvel.sha.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import bx.logging.Log
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.ui.characters.CharactersListScreen
import com.marvel.sha.ui.comics.ComicsListScreen
import kotlinx.coroutines.launch

@Composable @OptIn(ExperimentalFoundationApi::class)
internal fun HomePager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    pages: Array<HomePage>,
    onCharacterClick: (MarvelCharacter) -> Unit,
) {
    Column(modifier) {
        val coroutineScope = rememberCoroutineScope()

        TabRow(selectedTabIndex = pagerState.currentPage) {
            pages.forEachIndexed { index, page ->
                val title = stringResource(id = page.title)
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = title) },
                    unselectedContentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { index ->
            when (pages[index]) {
                HomePage.CHARACTERS -> CharactersListScreen(onClick = onCharacterClick)
                HomePage.COMICS     -> ComicsListScreen(onClick = { Log.info { it } })
                else                -> Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(pages[index].toString())
                }
            }
        }
    }
}
