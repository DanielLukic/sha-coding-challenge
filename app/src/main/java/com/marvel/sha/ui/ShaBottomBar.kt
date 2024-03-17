package com.marvel.sha.ui

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.marvel.sha.R

@Composable
internal fun ShaBottomBar(screen: MutableState<ShaPage>) = BottomAppBar {
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
