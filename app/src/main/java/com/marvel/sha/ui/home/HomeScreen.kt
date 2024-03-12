/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.marvel.sha.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.domain.MarvelComic

@Composable @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    pages: Array<HomePage> = HomePage.entries.toTypedArray(),
    onCharacterClick: (MarvelCharacter) -> Unit,
    onComicClick: (MarvelComic) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { HomeBar(scrollBehavior = scrollBehavior) }
    ) { contentPadding ->
        HomePager(
            modifier = Modifier.padding(top = contentPadding.calculateTopPadding()),
            pagerState = pagerState,
            pages = pages,
            onCharacterClick = onCharacterClick,
            onComicClick = onComicClick,
        )
    }
}
