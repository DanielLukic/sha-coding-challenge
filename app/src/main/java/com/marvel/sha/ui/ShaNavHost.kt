package com.marvel.sha.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marvel.sha.ui.Screen.*
import com.marvel.sha.ui.Screen.ComicDetail
import com.marvel.sha.ui.characters.CharacterDetailScreen
import com.marvel.sha.ui.comics.ComicDetailScreen
import com.marvel.sha.ui.creators.CreatorDetailScreen

@Composable
internal fun ShaNavHost(navController: NavHostController) {
    val screen = remember { mutableStateOf(ShaPage.COMICS) }
    NavHost(
        navController = navController,
        startDestination = Home.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        composable(route = Home.route) {
            fun goto(route: String) = navController.navigate(route)
            ShaHomeScreen(
                screen = screen,
                onCharacterClick = { goto(CharacterDetail(it.id)) },
                onComicClick = { goto(ComicDetail(it.id)) },
                onCreatorClick = { goto(CreatorDetail(it.id)) }
            )
        }
        composable(
            route = CharacterDetail.route,
            arguments = CharacterDetail.args,
        ) {
            CharacterDetailScreen(onBackClick = { navController.navigateUp() })
        }
        composable(
            route = ComicDetail.route,
            arguments = ComicDetail.args,
        ) {
            ComicDetailScreen(onBackClick = { navController.navigateUp() })
        }
        composable(
            route = CreatorDetail.route,
            arguments = CreatorDetail.args,
        ) {
            CreatorDetailScreen(onBackClick = { navController.navigateUp() })
        }
    }
}
