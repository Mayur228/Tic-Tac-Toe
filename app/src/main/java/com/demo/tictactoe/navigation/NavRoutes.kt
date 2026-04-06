package com.demo.tictactoe.navigation

sealed class NavRoutes(val route: String) {

    data object Permission : NavRoutes("permission")
    data object Home : NavRoutes("home")
    data object Host : NavRoutes("host")
    data object Scan : NavRoutes("scan")
    data object Waiting : NavRoutes("waiting")

    data object Game : NavRoutes("game/{isSinglePlayer}") {

        fun createRoute(isSinglePlayer: Boolean): String {
            return "game/$isSinglePlayer"
        }
    }
}