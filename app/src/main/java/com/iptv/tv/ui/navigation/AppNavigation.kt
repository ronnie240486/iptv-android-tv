package com.iptv.tv.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iptv.tv.ui.screens.*
import com.iptv.tv.ui.player.PlayerScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object LiveTV : Screen("livetv")
    object Movies : Screen("movies")
    object Series : Screen("series")
    object Radios : Screen("radios")
    object Search : Screen("search")
    object EPG : Screen("epg")
    object Settings : Screen("settings")
    object CineIA : Screen("cineia")
    object Roulette : Screen("roulette")
    object Player : Screen("player/{url}/{title}") {
        fun createRoute(url: String, title: String) = "player/$url/$title"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.LiveTV.route) {
            LiveTVScreen(
                navController = navController,
                onPlayChannel = { url, title ->
                    navController.navigate(Screen.Player.createRoute(
                        java.net.URLEncoder.encode(url, "UTF-8"),
                        java.net.URLEncoder.encode(title, "UTF-8")
                    ))
                }
            )
        }
        composable(Screen.Movies.route) {
            MoviesScreen(navController = navController, onPlay = { url, title ->
                navController.navigate(Screen.Player.createRoute(
                    java.net.URLEncoder.encode(url, "UTF-8"),
                    java.net.URLEncoder.encode(title, "UTF-8")
                ))
            })
        }
        composable(Screen.Series.route) {
            SeriesScreen(navController = navController, onPlay = { url, title ->
                navController.navigate(Screen.Player.createRoute(
                    java.net.URLEncoder.encode(url, "UTF-8"),
                    java.net.URLEncoder.encode(title, "UTF-8")
                ))
            })
        }
        composable(Screen.Radios.route) {
            RadiosScreen(navController = navController, onPlay = { url, title ->
                navController.navigate(Screen.Player.createRoute(
                    java.net.URLEncoder.encode(url, "UTF-8"),
                    java.net.URLEncoder.encode(title, "UTF-8")
                ))
            })
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController, onPlay = { url, title ->
                navController.navigate(Screen.Player.createRoute(
                    java.net.URLEncoder.encode(url, "UTF-8"),
                    java.net.URLEncoder.encode(title, "UTF-8")
                ))
            })
        }
        composable(Screen.EPG.route) {
            EPGScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(Screen.CineIA.route) {
            CineIAScreen(navController = navController, onPlay = { url, title ->
                navController.navigate(Screen.Player.createRoute(
                    java.net.URLEncoder.encode(url, "UTF-8"),
                    java.net.URLEncoder.encode(title, "UTF-8")
                ))
            })
        }
        composable(Screen.Roulette.route) {
            RouletteScreen(navController = navController, onPlay = { url, title ->
                navController.navigate(Screen.Player.createRoute(
                    java.net.URLEncoder.encode(url, "UTF-8"),
                    java.net.URLEncoder.encode(title, "UTF-8")
                ))
            })
        }
        composable(
            Screen.Player.route,
            arguments = listOf(
                navArgument("url") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val url = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("url") ?: "", "UTF-8")
            val title = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
            PlayerScreen(url = url, title = title, onBack = { navController.popBackStack() })
        }
    }
}
