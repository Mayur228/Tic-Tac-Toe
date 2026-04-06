package com.demo.tictactoe.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.demo.tictactoe.ui.game.GameBoardScreen
import com.demo.tictactoe.ui.gamehost.GameViewModel
import com.demo.tictactoe.ui.home.HomeScreen
import com.demo.tictactoe.ui.gamehost.HostScreen
import com.demo.tictactoe.ui.scan.ScanScreen
import com.demo.tictactoe.ui.waiting.WaitingScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    val viewModel: GameViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {

        // ------------------ HOME ------------------
        composable(NavRoutes.Home.route) {
            HomeScreen(
                onHostClick = {
                    navController.navigate(NavRoutes.Host.route)
                },
                onJoinClick = {
                    navController.navigate(NavRoutes.Scan.route)
                },
                onAiClick = {
                    navController.navigate(
                        NavRoutes.Game.createRoute(true) // ✅ AI
                    )
                }
            )
        }

        // ------------------ HOST ------------------
        composable(NavRoutes.Host.route) {
            HostScreen(
                viewModel = viewModel,
                onConnected = {
                    navController.navigate(NavRoutes.Waiting.route)
                }
            )
        }

        // ------------------ WAITING ------------------
        composable(NavRoutes.Waiting.route) {
            WaitingScreen(
                viewModel = viewModel,
                onConnected = {
                    navController.navigate(
                        NavRoutes.Game.createRoute(false) // ✅ Multiplayer
                    ) {
                        popUpTo(NavRoutes.Home.route)
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // ------------------ GAME ------------------
        composable(
            route = NavRoutes.Game.route,
            arguments = listOf(
                androidx.navigation.navArgument("isSinglePlayer") {
                    type = androidx.navigation.NavType.BoolType
                }
            )
        ) { backStackEntry ->

            val isSinglePlayer =
                backStackEntry.arguments?.getBoolean("isSinglePlayer") ?: false

            GameBoardScreen(
                isSinglePlayer = isSinglePlayer
            )
        }

        // ------------------ SCAN ------------------
        composable(NavRoutes.Scan.route) {
            ScanScreen(
                onConnected = {
                    navController.navigate(
                        NavRoutes.Game.createRoute(false) // ✅ Multiplayer
                    )
                }
            )
        }
    }
}