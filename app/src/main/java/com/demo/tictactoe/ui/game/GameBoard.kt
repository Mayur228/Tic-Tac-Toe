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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.tictactoe.ui.game.component.DifficultyDialog
import com.demo.tictactoe.ui.game.component.FirstMoveDialog
import com.demo.tictactoe.ui.game.component.WinDialog
import com.demo.tictactoe.ui.game.component.WinLineOverlay
import com.demo.tictactoe.ui.gamehost.ConnectionState
import com.demo.tictactoe.ui.gamehost.GameViewModel

@Composable
fun GameBoardScreen(isSinglePlayer: Boolean) {

    val viewModel: GameBoardViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(isSinglePlayer) {
        if (isSinglePlayer) {
            viewModel.startSinglePlayer()
        }
    }

    if (state.showDifficultyDialog) {
        DifficultyDialog(
            onSelect = viewModel::onDifficultySelected
        )
    }

    if (state.showFirstMoveDialog) {
        FirstMoveDialog(
            isSinglePlayer = state.isSinglePlayer,
            onResult = viewModel::selectFirstPlayer
        )
    }

    if (state.gameOver) {
        WinDialog(
            isWinner = state.winner == state.myMark,
            isDraw = state.winner == null,
            onPlayAgain = viewModel::resetGame
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
            color = Color.White,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            for (row in 0..2) {
                Row {
                    for (col in 0..2) {
                        val pos = row * 3 + col
                        val value = state.board[pos]

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .padding(6.dp)
                                .background(Color(0xFF0F1A30), RoundedCornerShape(12.dp))
                                .clickable(
                                    enabled = !state.gameOver &&
                                            state.isMyTurn &&
                                            value.isEmpty()
                                ) {
                                    viewModel.makeMove(pos)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = value,
                                fontSize = 36.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
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
