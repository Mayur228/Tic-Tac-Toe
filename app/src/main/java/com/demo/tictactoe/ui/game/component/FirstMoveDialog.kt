package com.demo.tictactoe.ui.game.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun FirstMoveDialog(
    isSinglePlayer: Boolean,
    onResult: (Boolean) -> Unit
) {

    var rotation by remember { mutableStateOf(0f) }
    var result by remember { mutableStateOf<String?>(null) }

    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(
            durationMillis = 2500,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    // Motion blur illusion (stretch when spinning fast)
    val blurScale by animateFloatAsState(
        targetValue = if (rotation != 0f && result == null) 1.25f else 1f,
        animationSpec = tween(600),
        label = ""
    )

    // Shadow scaling
    val shadowScale by animateFloatAsState(
        targetValue = if (rotation != 0f && result == null) 0.6f else 1f,
        animationSpec = tween(600),
        label = ""
    )

    val normalizedRotation = animatedRotation % 360
    val isFront = normalizedRotation < 90f || normalizedRotation > 270f

    LaunchedEffect(Unit) {

        rotation = 1800f // 5 full flips

        delay(2500)

        val meFirst = (0..1).random() == 0

        result = if (meFirst) {
            "YOU"
        } else {
            if (isSinglePlayer) "AI" else "OPP"
        }

        delay(900)
        onResult(meFirst)
    }

    AlertDialog(
        onDismissRequest = {},
        containerColor = Color(0xFF0E1B3D),
        shape = RoundedCornerShape(28.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Coin Toss",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    // Shadow
                    Box(
                        modifier = Modifier
                            .offset(y = 70.dp)
                            .size(90.dp)
                            .graphicsLayer {
                                scaleX = shadowScale
                                scaleY = 0.4f * shadowScale
                                alpha = 0.4f
                            }
                            .background(
                                Color.Black,
                                CircleShape
                            )
                    )

                    // Coin
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .graphicsLayer {
                                rotationY = animatedRotation
                                cameraDistance = 14 * density
                                scaleX = blurScale
                            }
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFFFF8DC),   // highlight
                                        Color(0xFFFFD700),   // gold
                                        Color(0xFFB8860B)    // dark gold edge
                                    )
                                ),
                                shape = CircleShape
                            )
                            .border(
                                4.dp,
                                Color(0xFFFFC107),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = if (result == null) {
                                if (isFront) "X" else "O"
                            } else {
                                result!!
                            },
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF081220)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                if (result != null) {
                    Text(
                        text = if (result == "YOU")
                            "You move first!"
                        else
                            "${if (isSinglePlayer) "AI" else "Opponent"} moves first!",
                        fontSize = 16.sp,
                        color = Color(0xFFB5C7E0)
                    )
                }
            }
        },
        confirmButton = {}
    )
}