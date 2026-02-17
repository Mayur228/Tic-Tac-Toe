package com.demo.tictactoe.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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

    if (state.gameOver) {
        WinDialog(
            isWinner = state.winner == state.myMark,
            isDraw = state.winner == null,
            onPlayAgain = {
                viewModel.resetGame()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ConnectionBadge(state.connectionState)

        // ---------------- HEADER CARD ----------------

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0E1B3D)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = if (state.connectionState == ConnectionState.Connected) {
                        if (state.isMyTurn) "Your Turn" else "Opponent's Turn"
                    } else {
                        state.statusText
                    },
                    fontSize = 22.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "You are: ${state.myMark}",
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- BOARD ----------------

        Box(
            modifier = Modifier
                .size(boardSize)
                .shadow(12.dp, RoundedCornerShape(20.dp))
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

                            val isWinningCell = state.winningLine?.contains(pos) == true

                            val scale by animateFloatAsState(
                                targetValue = if (isWinningCell) 1.1f else 1f,
                                label = ""
                            )

                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .padding(6.dp)
                                    .background(
                                        if (isWinningCell)
                                            Color(0xFF1B5E20)
                                        else
                                            cellColor,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = LocalIndication.current,
                                        enabled = state.connectionState == ConnectionState.Connected &&
                                                !state.gameOver &&
                                                state.isMyTurn &&
                                                value.isEmpty(),
                                        onClick = {
                                            viewModel.makeMove(pos)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                AnimatedContent(
                                    targetState = value,
                                    label = ""
                                ) { target ->
                                    Text(
                                        text = target,
                                        fontSize = 42.sp,
                                        color = when (target) {
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
            }

            if (state.winningLine != null) {
                WinLineOverlay(state.winningLine!!)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun ConnectionBadge(state: ConnectionState) {

    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            ConnectionState.Connected -> Color(0xFF2E7D32)
            ConnectionState.Connecting -> Color(0xFFFFA000)
            ConnectionState.Scanning -> Color(0xFF1976D2)
            ConnectionState.Advertising -> Color(0xFF7B1FA2)
            ConnectionState.Failed -> Color(0xFFD32F2F)
            else -> Color.Gray
        },
        label = "connectionColor"
    )

    val pulseAlpha by animateFloatAsState(
        targetValue = if (state == ConnectionState.Connected) 1f else 0.6f,
        label = "pulse"
    )

    Row(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .background(backgroundColor, RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Status Dot
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    Color.White.copy(alpha = pulseAlpha),
                    shape = RoundedCornerShape(50)
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = state.name,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
