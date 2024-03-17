package com.marvel.sha.ui

import android.app.Activity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.marvel.sha.R
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.domain.MarvelCreator
import com.marvel.sha.ui.ShaPage.*
import com.marvel.sha.ui.characters.CharacterListScreen
import com.marvel.sha.ui.comics.ComicListScreen
import com.marvel.sha.ui.creators.CreatorListScreen

@Composable @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun ShaHomeScreen(
    modifier: Modifier = Modifier,
    screen: MutableState<ShaPage> = remember { mutableStateOf(COMICS) },
    onCharacterClick: (MarvelCharacter) -> Unit,
    onComicClick: (MarvelComic) -> Unit,
    onCreatorClick: (MarvelCreator) -> Unit,
) {
    val activity = LocalContext.current as Activity
    val portrait = calculateWindowSizeClass(activity).heightSizeClass != WindowHeightSizeClass.Compact

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = if (portrait) modifier.nestedScroll(scrollBehavior.nestedScrollConnection) else modifier,
        topBar = {
            if (portrait) TopAppBar(
                colors = topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Marvel Super Hero App",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                },
            )
        },
        bottomBar = { if (portrait) ShaBottomBar(screen) }
    ) { padding ->
        Row(modifier = Modifier.padding(padding)) {
            if (!portrait) ShaNavRail(screen)
            when (screen.value) {
                CHARACTERS -> CharacterListScreen(onClick = onCharacterClick)
                COMICS     -> ComicListScreen(onClick = onComicClick)
                CREATORS   -> CreatorListScreen(onClick = onCreatorClick)
            }
        }
    }
}

@Composable
internal fun ShaBottomBar(screen: MutableState<ShaPage>) = BottomAppBar {
    NavigationBarItem(
        selected = screen.value == COMICS,
        icon = {},
        label = { Text(stringResource(R.string.comics)) },
        onClick = { screen.value = COMICS },
    )
    NavigationBarItem(
        selected = screen.value == CHARACTERS,
        icon = {},
        label = { Text(stringResource(R.string.characters)) },
        onClick = { screen.value = CHARACTERS },
    )
    NavigationBarItem(
        selected = screen.value == CREATORS,
        icon = {},
        label = { Text(stringResource(R.string.creators)) },
        onClick = { screen.value = CREATORS },
    )
}

@Composable
internal fun ShaNavRail(screen: MutableState<ShaPage>) = NavigationRail {
    NavigationRailItem(
        selected = screen.value == COMICS,
        icon = {},
        label = { Text(stringResource(R.string.comics)) },
        onClick = { screen.value = COMICS },
    )
    NavigationRailItem(
        selected = screen.value == CHARACTERS,
        icon = {},
        label = { Text(stringResource(R.string.characters)) },
        onClick = { screen.value = CHARACTERS },
    )
    NavigationRailItem(
        selected = screen.value == CREATORS,
        icon = {},
        label = { Text(stringResource(R.string.creators)) },
        onClick = { screen.value = CREATORS },
    )
}
