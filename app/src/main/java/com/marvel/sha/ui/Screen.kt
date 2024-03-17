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
        operator fun invoke(characterId: Int) = "character/${characterId}"
    }

    data object ComicDetail : Screen(
        route = "comic/{comicId}",
        args = listOf(navArgument("comicId") { type = NavType.StringType }),
    ) {
        operator fun invoke(comicId: Int) = "comic/${comicId}"
    }

    data object CreatorDetail : Screen(
        route = "creator/{creatorId}",
        args = listOf(navArgument("creatorId") { type = NavType.StringType }),
    ) {
        operator fun invoke(creatorId: Int) = "creator/${creatorId}"
    }
}
