package com.demo.tictactoe.ui.game.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun WinDialog(
    isWinner: Boolean,
    isDraw: Boolean,
    onPlayAgain: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {},
        containerColor = Color(0xFF0F1A30),
        shape = RoundedCornerShape(20.dp),

        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        isDraw -> "🤝 Draw!"
                        isWinner -> "🎉 You Win!"
                        else -> "😢 You Lost!"
                    },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },

        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        isDraw -> "No more moves left!"
                        isWinner -> "Great job! You defeated your opponent!"
                        else -> "Better luck next time."
                    },
                    fontSize = 16.sp,
                    color = Color(0xFFB5C7E0)
                )
            }
        },

        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onPlayAgain,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C9EFF)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.width(160.dp)
                ) {
                    Text("Play Again", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    )
}
