package com.marvel.sha.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Greeting(name: String, modifier: Modifier = Modifier) = Text(
    text = "Hello $name!",
    modifier = modifier,
)
