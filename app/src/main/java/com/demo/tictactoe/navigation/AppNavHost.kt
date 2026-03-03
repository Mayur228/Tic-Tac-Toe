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
        startDestination = NavRoutes.HOME
    ) {

        // ------------------ HOME ------------------
        composable(NavRoutes.HOME) {
            HomeScreen(
                onHostClick = {
                    navController.navigate(NavRoutes.HOST)
                },
                onJoinClick = {
                    navController.navigate(NavRoutes.SCAN)
                },
                onAiClick = {
                    viewModel.startSinglePlayer()
                    navController.navigate(NavRoutes.GAME)
                }
            )
        }


        // ------------------ HOST ------------------
        composable(NavRoutes.HOST) {
            HostScreen(
                viewModel = viewModel,
                onConnected = {
                    navController.navigate(NavRoutes.WAITING)
                }
            )
        }

        // ------------------ WAITING ------------------
        composable(NavRoutes.WAITING) {
            WaitingScreen(
                viewModel = viewModel,
                onConnected = {
                    navController.navigate(NavRoutes.GAME) {
                        popUpTo(NavRoutes.HOME)
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }


        // ------------------ GAME ------------------
        composable(NavRoutes.GAME) {
            GameBoardScreen(viewModel)
        }

        // ------------------ SCAN ------------------
        composable(NavRoutes.SCAN) {
            ScanScreen(
                viewModel = viewModel,
                onConnected = {
                    navController.navigate(NavRoutes.GAME)
                }
            )
        }
    }
}
