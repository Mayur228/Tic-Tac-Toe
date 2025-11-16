package com.demo.tictactoe.ui.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.times

@Composable
fun WinLineOverlay(
    winningLine: List<Int>,
    boardSize: Dp = 330.dp
) {
    val lineColor = Color(0xFF00E5FF)
    val stroke = 8.dp

    Box(
        modifier = Modifier
            .size(boardSize)
            .padding(12.dp)
    ) {

        val cellSize = (boardSize - 24.dp) / 3

        when (winningLine) {
            // Horizontals
            listOf(0, 1, 2) -> HorizontalLine(0, cellSize, stroke, lineColor)
            listOf(3, 4, 5) -> HorizontalLine(1, cellSize, stroke, lineColor)
            listOf(6, 7, 8) -> HorizontalLine(2, cellSize, stroke, lineColor)

            // Verticals
            listOf(0, 3, 6) -> VerticalLine(0, cellSize, stroke, lineColor)
            listOf(1, 4, 7) -> VerticalLine(1, cellSize, stroke, lineColor)
            listOf(2, 5, 8) -> VerticalLine(2, cellSize, stroke, lineColor)

            // Diagonals
            listOf(0, 4, 8) -> SlashLine(true, stroke, lineColor)
            listOf(2, 4, 6) -> SlashLine(false, stroke, lineColor)
        }
    }
}

@Composable
private fun HorizontalLine(
    row: Int,
    cell: Dp,
    stroke: Dp,
    color: Color
) {
    Box(
        modifier = Modifier
            .offset(y = row * cell + cell / 2 - stroke / 2)
            .fillMaxWidth()
            .height(stroke)
            .background(color, RoundedCornerShape(50))
    )
}

@Composable
private fun VerticalLine(
    col: Int,
    cell: Dp,
    stroke: Dp,
    color: Color
) {
    Box(
        modifier = Modifier
            .offset(x = col * cell + cell / 2 - stroke / 2)
            .fillMaxHeight()
            .width(stroke)
            .background(color, RoundedCornerShape(50))
    )
}

@Composable
private fun SlashLine(
    leftToRight: Boolean,
    stroke: Dp,
    color: Color
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center   // ← Use this instead of Modifier.align()
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = if (leftToRight) 45f else -45f
                }
                .width(stroke)
                .height(stroke * 12)  // Long enough to cross all 3 cells
                .background(color, RoundedCornerShape(50))
        )
    }
}

