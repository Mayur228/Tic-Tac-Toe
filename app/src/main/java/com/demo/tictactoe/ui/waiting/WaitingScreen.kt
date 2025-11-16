package com.demo.tictactoe.ui.waiting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.tictactoe.ui.gamehost.GameViewModel

@Composable
fun WaitingScreen(
    viewModel: GameViewModel,
    onConnected: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // Navigate when statusText indicates connection (e.g. "Connected" or "Connected with ...")
    LaunchedEffect(state.statusText) {
        if (state.statusText.startsWith("Connected")) {
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
                text = state.statusText,
                fontSize = 26.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please wait…",
                fontSize = 16.sp,
                color = Color(0xFF9BB6D1)
            )
        }
    }
}