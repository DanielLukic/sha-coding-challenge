package com.marvel.sha.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

internal sealed class Screen(
    val route: String,
    val args: List<NamedNavArgument> = emptyList(),
) {
    data object Home : Screen("home")

    data object CharacterDetail : Screen(
        route = "character/{characterId}",
        args = listOf(navArgument("characterId") { type = NavType.StringType }),
    ) {
        fun createRoute(characterId: String) = "character/${characterId}"
    }
}
