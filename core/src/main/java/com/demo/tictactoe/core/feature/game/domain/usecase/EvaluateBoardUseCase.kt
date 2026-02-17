package com.demo.tictactoe.core.feature.game.domain.usecase

import com.demo.tictactoe.core.feature.game.data.model.GameResult
import org.koin.core.annotation.Factory

@Factory
class EvaluateBoardUseCase {

    operator fun invoke(board: List<String>): GameResult {

        val wins = listOf(
            listOf(0,1,2),
            listOf(3,4,5),
            listOf(6,7,8),
            listOf(0,3,6),
            listOf(1,4,7),
            listOf(2,5,8),
            listOf(0,4,8),
            listOf(2,4,6)
        )

        for (line in wins) {
            val (a,b,c) = line

            if (board[a].isNotEmpty() &&
                board[a] == board[b] &&
                board[b] == board[c]
            ) {
                return GameResult.Win(
                    winner = board[a],
                    winningLine = line
                )
            }
        }

        if (board.all { it.isNotEmpty() }) {
            return GameResult.Draw
        }

        return GameResult.Ongoing
    }
}
