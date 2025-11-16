package com.demo.tictactoe.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.tictactoe.ui.game.component.WinDialog
import com.demo.tictactoe.ui.game.component.WinLineOverlay
import com.demo.tictactoe.ui.gamehost.ConnectionState
import com.demo.tictactoe.ui.gamehost.GameViewModel

@Composable
fun GameBoardScreen(viewModel: GameViewModel) {

    val state by viewModel.state.collectAsState()

    val boardSize = 330.dp
    val cellColor = Color(0xFF0F1A30)
    val borderColor = Color(0xFF22304A)

    // Show dialog separately
    if (state.gameOver) {
        WinDialog(
            isWinner = state.winner == state.myMark,
            isDraw = state.winner == null,
            onPlayAgain = { viewModel.resetGame() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = state.statusText,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(24.dp)
        )

        Box(
            modifier = Modifier
                .size(boardSize)
                .background(Color(0xFF081220), RoundedCornerShape(20.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {

            Column {
                for (row in 0..2) {
                    Row {
                        for (col in 0..2) {
                            val pos = row * 3 + col
                            val value = state.board[pos]

                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .padding(6.dp)
                                    .background(cellColor, RoundedCornerShape(16.dp))
                                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                                    .clickable(
                                        enabled = state.connectionState == ConnectionState.Connected &&
                                                !state.gameOver &&
                                                state.isMyTurn &&
                                                value.isEmpty(),
                                        onClick = { viewModel.makeMove(pos) }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = value,
                                    fontSize = 42.sp,
                                    color = when (value) {
                                        "X" -> Color(0xFF3FA7FF)
                                        "O" -> Color(0xFFFF6DD0)
                                        else -> Color.Transparent
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (state.winningLine != null) {
                WinLineOverlay(state.winningLine!!)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}