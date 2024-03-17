package com.marvel.sha.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.domain.MarvelCreator
import com.marvel.sha.ui.ShaPage.*
import com.marvel.sha.ui.characters.CharacterListScreen
import com.marvel.sha.ui.comics.ComicListScreen
import com.marvel.sha.ui.creators.CreatorListScreen
import org.koin.androidx.compose.koinViewModel

@Composable @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
internal fun ShaHomeScreen(
    modifier: Modifier = Modifier,
    model: ShaHomeViewModel = koinViewModel(),
    screen: MutableState<ShaPage> = remember { mutableStateOf(COMICS) },
    onCharacterClick: (MarvelCharacter) -> Unit,
    onComicClick: (MarvelComic) -> Unit,
    onCreatorClick: (MarvelCreator) -> Unit,
) {
    val activity = LocalContext.current as Activity
    val portrait = calculateWindowSizeClass(activity).heightSizeClass != WindowHeightSizeClass.Compact
    val infoShowing = remember { mutableStateOf(false) }
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
                actions = {
                    IconButton(onClick = { infoShowing.value = true }) {
                        Icon(Icons.Default.Info, contentDescription = "Show info")
                    }
                }
            )
        },
        bottomBar = { if (portrait) ShaBottomBar(screen) },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Row {
                if (!portrait) ShaNavRail(screen) {
                    infoShowing.value = true
                }
                when (screen.value) {
                    CHARACTERS -> CharacterListScreen(onClick = onCharacterClick)
                    COMICS     -> ComicListScreen(onClick = onComicClick)
                    CREATORS   -> CreatorListScreen(onClick = onCreatorClick)
                }
            }
            if (infoShowing.value) {
                val attribution = model.attributionFlow.collectAsStateWithLifecycle()
                AlertDialog(
                    onDismissRequest = { infoShowing.value = false },
                    title = { Text("Marvel Super Hero App") },
                    text = { LazyColumn { item { Text(attribution.value) } } },
                    confirmButton = { TextButton(onClick = { infoShowing.value = false }) { Text("OK") } },
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}
