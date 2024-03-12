package com.marvel.sha.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marvel.sha.ui.characters.CharacterDetailScreen
import com.marvel.sha.ui.comics.ComicDetailScreen
import com.marvel.sha.ui.home.HomeScreen

@Composable
internal fun ShaNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onCharacterClick = {
                    navController.navigate(
                        Screen.CharacterDetail.createRoute(it.id.toString())
                    )
                },
                onComicClick = {
                    navController.navigate(
                        Screen.ComicDetail.createRoute(it.id.toString())
                    )
                }
            )
        }
        composable(
            route = Screen.CharacterDetail.route,
            arguments = Screen.CharacterDetail.args,
        ) {
            CharacterDetailScreen(onBackClick = { navController.navigateUp() })
        }
        composable(
            route = Screen.ComicDetail.route,
            arguments = Screen.ComicDetail.args,
        ) {
            ComicDetailScreen(onBackClick = { navController.navigateUp() })
        }
    }
}
