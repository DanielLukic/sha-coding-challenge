package com.marvel.sha.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.marvel.sha.R
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.domain.MarvelCreator
import com.marvel.sha.ui.characters.CharacterListScreen
import com.marvel.sha.ui.comics.ComicListScreen
import com.marvel.sha.ui.creators.CreatorListScreen

@Composable @OptIn(ExperimentalMaterial3Api::class)
internal fun ShaHomeScreen(
    modifier: Modifier = Modifier,
    screen: MutableState<ShaPage> = remember { mutableStateOf(ShaPage.COMICS) },
    onCharacterClick: (MarvelCharacter) -> Unit,
    onComicClick: (MarvelComic) -> Unit,
    onCreatorClick: (MarvelCreator) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Marvel Super Hero App",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                },
            )
        },
        bottomBar = {
            BottomAppBar {
                NavigationBarItem(
                    selected = screen.value == ShaPage.COMICS,
                    icon = {},
                    label = { Text(stringResource(R.string.comics)) },
                    onClick = { screen.value = ShaPage.COMICS },
                )
                NavigationBarItem(
                    selected = screen.value == ShaPage.CHARACTERS,
                    icon = {},
                    label = { Text(stringResource(R.string.characters)) },
                    onClick = { screen.value = ShaPage.CHARACTERS },
                )
                NavigationBarItem(
                    selected = screen.value == ShaPage.CREATORS,
                    icon = {},
                    label = { Text(stringResource(R.string.creators)) },
                    onClick = { screen.value = ShaPage.CREATORS },
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (screen.value) {
                ShaPage.CHARACTERS -> CharacterListScreen(onClick = onCharacterClick)
                ShaPage.COMICS     -> ComicListScreen(onClick = onComicClick)
                ShaPage.CREATORS   -> CreatorListScreen(onClick = onCreatorClick)
            }
        }
    }
}
