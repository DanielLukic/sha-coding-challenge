package com.marvel.sha.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marvel.sha.ui.characters.CharacterDetail
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
            )
        }
        composable(
            route = Screen.CharacterDetail.route,
            arguments = Screen.CharacterDetail.args,
        ) {
            CharacterDetail(onBackClick = { navController.navigateUp() })
        }
    }
}
