package com.demo.tictactoe.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onHostClick: () -> Unit,
    onJoinClick: () -> Unit,
    onAiClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Tic Tac Toe",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(50.dp))

        HomeButton(
            text = "Host Game",
            color = Color(0xFF4C9EFF),
            onClick = onHostClick
        )

        Spacer(Modifier.height(20.dp))

        HomeButton(
            text = "Join Game",
            color = Color(0xFFFF61C6),
            onClick = onJoinClick
        )

        Spacer(Modifier.height(20.dp))

        HomeButton(
            text = "Play vs AI",
            color = Color(0xFF00C853),
            onClick = onAiClick
        )

    }
}

@Composable
private fun HomeButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(230.dp)
            .height(55.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
