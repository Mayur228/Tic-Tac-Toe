package com.demo.tictactoe.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.demo.tictactoe.ui.game.GameBoardScreen
import com.demo.tictactoe.ui.gamehost.GameViewModel
import com.demo.tictactoe.ui.gamehost.HostJoinScreen
import com.demo.tictactoe.ui.scan.ScanScreen
import com.demo.tictactoe.ui.waiting.WaitingScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    val viewModel: GameViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOST
    ) {

        // ------------------ HOST / JOIN ------------------
        composable(NavRoutes.HOST) {
            HostJoinScreen(
                viewModel = viewModel,
                onHost = {
                    navController.navigate(NavRoutes.GAME)
                },
                onJoin = {
                    navController.navigate(NavRoutes.SCAN)
                }
            )
        }

        composable(NavRoutes.WAITING) {
            WaitingScreen(viewModel) {
                navController.navigate(NavRoutes.GAME)
            }
        }

        // ------------------ GAME SCREEN ------------------
        composable(NavRoutes.GAME) {
            GameBoardScreen(viewModel)
        }

        // ------------------ SCAN → GAME ------------------
        composable(NavRoutes.SCAN) {
            ScanScreen(viewModel = viewModel, onConnected = {
                navController.navigate(NavRoutes.GAME)
            })
        }
    }
}