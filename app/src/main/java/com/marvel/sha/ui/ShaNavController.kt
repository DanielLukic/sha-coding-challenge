package com.marvel.sha.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
internal fun SuperHeroApp() {
    val navController = rememberNavController()
    ShaNavHost(navController = navController)
}
