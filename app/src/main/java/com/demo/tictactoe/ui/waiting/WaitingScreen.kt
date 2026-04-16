package com.demo.tictactoe.ui.waiting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.tictactoe.ui.gamehost.ConnectionState
import com.demo.tictactoe.ui.gamehost.GameViewModel
import com.demo.tictactoe.ui.gamehost.HostState

@Composable
fun WaitingScreen(
    viewModel: GameViewModel,
    onConnected: () -> Unit,
    onCancel: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var hasNavigated by remember { mutableStateOf(false) }

    // ✅ Start hosting ONCE
    LaunchedEffect(Unit) {
        viewModel.startHosting()
    }

    // ✅ Navigate to Game
    LaunchedEffect(state) {
        if (!hasNavigated && state is HostState.Success) {
            val s = state as HostState.Success

            if (s.connectionState == ConnectionState.Connected) {
                hasNavigated = true
                onConnected()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B)),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            CircularProgressIndicator(color = Color(0xFF4C9EFF))

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = when (state) {
                    is HostState.Success -> (state as HostState.Success).statusText
                    is HostState.Error -> (state as HostState.Error).message
                    else -> "Preparing..."
                },
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.cancelConnection()
                    onCancel()
                }
            ) {
                Text("Cancel")
            }
        }
    }

    BackHandler {
        viewModel.cancelConnection()
        onCancel()
    }
}