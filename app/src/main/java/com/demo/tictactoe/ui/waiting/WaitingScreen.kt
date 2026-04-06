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
import com.demo.tictactoe.ui.gamehost.ConnectionState
import com.demo.tictactoe.ui.gamehost.GameViewModel

@Composable
fun WaitingScreen(
    viewModel: GameViewModel,
    onConnected: () -> Unit,
    onCancel: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Navigate when connected
    LaunchedEffect(state.connectionState) {
        if (state.connectionState == ConnectionState.Connected) {
            onConnected()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            CircularProgressIndicator(
                color = Color(0xFF4C9EFF),
                strokeWidth = 4.dp,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = when (state.connectionState) {
                    ConnectionState.Advertising -> "Waiting for player..."
                    ConnectionState.Connecting -> "Connecting..."
                    ConnectionState.Connected -> "Connected!"
                    ConnectionState.Failed -> "Connection Failed"
                    else -> "Preparing..."
                },
                fontSize = 24.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // -------- CANCEL BUTTON --------
            Button(
                onClick = {
                    //viewModel.cancelConnection()
                    onCancel()
                }
            ) {
                Text("Cancel")
            }
        }
    }

    BackHandler {
        //viewModel.cancelConnection()
        onCancel()
    }

}