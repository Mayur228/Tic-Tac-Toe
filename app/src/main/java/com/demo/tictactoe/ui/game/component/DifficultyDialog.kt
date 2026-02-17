package com.demo.tictactoe.ui.game.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.demo.tictactoe.ui.AiDifficulty

@Composable
fun DifficultyDialog(
    onSelect: (AiDifficulty) -> Unit
) {

    AlertDialog(
        onDismissRequest = {},
        containerColor = Color(0xFF0E1B3D),
        title = {
            Text(
                text = "Select Difficulty",
                color = Color.White
            )
        },
        text = {
            Column {

                DifficultyButton("Easy") {
                    onSelect(AiDifficulty.EASY)
                }

                Spacer(modifier = Modifier.height(12.dp))

                DifficultyButton("Medium") {
                    onSelect(AiDifficulty.MEDIUM)
                }

                Spacer(modifier = Modifier.height(12.dp))

                DifficultyButton("Hard") {
                    onSelect(AiDifficulty.HARD)
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun DifficultyButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}
