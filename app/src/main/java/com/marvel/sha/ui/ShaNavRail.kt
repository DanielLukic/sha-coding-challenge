package com.marvel.sha.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.marvel.sha.R

@Composable
internal fun ShaNavRail(
    screen: MutableState<ShaPage>,
    onInfoClick: () -> Unit,
) = NavigationRail {
    NavigationRailItem(
        selected = screen.value == ShaPage.COMICS,
        icon = {},
        label = { Text(stringResource(R.string.comics)) },
        onClick = { screen.value = ShaPage.COMICS },
    )
    NavigationRailItem(
        selected = screen.value == ShaPage.CHARACTERS,
        icon = {},
        label = { Text(stringResource(R.string.characters)) },
        onClick = { screen.value = ShaPage.CHARACTERS },
    )
    NavigationRailItem(
        selected = screen.value == ShaPage.CREATORS,
        icon = {},
        label = { Text(stringResource(R.string.creators)) },
        onClick = { screen.value = ShaPage.CREATORS },
    )
    Spacer(Modifier.weight(1f))
    IconButton(onClick = onInfoClick) {
        Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
    }
}
