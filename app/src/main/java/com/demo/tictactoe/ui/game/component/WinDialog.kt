package com.demo.tictactoe.ui.game.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WinDialog(
    isWinner: Boolean,
    isDraw: Boolean,
    onPlayAgain: () -> Unit
) {

    val glowColor by animateColorAsState(
        targetValue = when {
            isDraw -> Color(0xFFFFA000)
            isWinner -> Color(0xFF00E676)
            else -> Color(0xFFD50000)
        },
        label = ""
    )

    AlertDialog(
        onDismissRequest = {},
        containerColor = Color(0xFF0E1B3D),
        shape = RoundedCornerShape(28.dp),

        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = when {
                        isDraw -> "DRAW"
                        isWinner -> "VICTORY"
                        else -> "DEFEAT"
                    },
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = glowColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when {
                        isDraw -> "No moves left."
                        isWinner -> "Brilliant strategy!"
                        else -> "AI was stronger this time."
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
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = glowColor
                    ),
                    modifier = Modifier
                        .width(180.dp)
                        .height(48.dp)
                        .border(
                            2.dp,
                            glowColor.copy(alpha = 0.6f),
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        "PLAY AGAIN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    )
}
