package com.demo.tictactoe.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.tictactoe.common.GameConfig.RESET_CODE
import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.feature.game.data.model.GameResult
import com.demo.tictactoe.core.feature.game.domain.usecase.EvaluateBoardUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.ObserveMovesUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.SendMoveUseCase
import com.demo.tictactoe.ui.AiDifficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameBoardViewModel @Inject constructor(
    private val observeMovesUseCase: ObserveMovesUseCase,
    private val sendMoveUseCase: SendMoveUseCase,
    private val evaluateBoardUseCase: EvaluateBoardUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(GameBoardState())
    val state = _state.asStateFlow()

    private var observeJob: Job? = null

    init {
        observeIncomingMoves()
    }

    // ---------------- OBSERVE MOVES ----------------
    private fun observeIncomingMoves() {
        observeJob?.cancel()

        observeJob = viewModelScope.launch {
            observeMovesUseCase().collectLatest { move ->
                val current = _state.value

                if (current.isSinglePlayer) return@collectLatest

                if (move == RESET_CODE) {
                    resetGame(true)
                } else {
                    applyOpponentMove(move)
                }
            }
        }
    }

    // ---------------- SINGLE PLAYER ----------------
    fun startSinglePlayer() {
        _state.update {
            it.copy(
                isSinglePlayer = true,
                showDifficultyDialog = true
            )
        }
    }

    fun onDifficultySelected(difficulty: AiDifficulty) {
        _state.update {
            it.copy(
                aiDifficulty = difficulty,
                showDifficultyDialog = false,
                showFirstMoveDialog = true
            )
        }
    }

    fun selectFirstPlayer(isMeFirst: Boolean) {
        val myMark = if (isMeFirst) "X" else "O"

        _state.update {
            it.copy(
                myMark = myMark,
                isMyTurn = isMeFirst,
                showFirstMoveDialog = false,
                isFirstMoveDecided = true,
                statusText = if (isMeFirst) "Your turn" else "Opponent's turn"
            )
        }
    }

    // ---------------- MAKE MOVE ----------------
    fun makeMove(pos: Int) {
        val s = _state.value

        if (s.gameOver) return
        if (!s.isMyTurn) return
        if (s.board[pos].isNotEmpty()) return

        if (s.isSinglePlayer) {
            applyLocalMove(pos)
        } else {
            viewModelScope.launch {
                runCatching {
                    sendMoveUseCase(pos)
                }
                applyLocalMove(pos)
            }
        }
    }

    // ---------------- LOCAL MOVE ----------------
    private fun applyLocalMove(pos: Int) {
        val s = _state.value
        val board = s.board.toMutableList()
        board[pos] = s.myMark

        viewModelScope.launch {
            when (val result = evaluateBoardUseCase(board)) {
                is Resource.Data -> {
                    when (val res = result.value) {
                        GameResult.Draw -> drawGame(board)
                        GameResult.Ongoing -> _state.update {
                            it.copy(
                                board = board,
                                isMyTurn = false,
                                statusText = "Waiting for opponent..."
                            )
                        }
                        is GameResult.Win -> {
                            endGame(board, res.winner, res.winningLine)
                        }
                    }
                }
                is Resource.Error -> {}
            }
        }
    }

    // ---------------- OPPONENT MOVE ----------------
    private fun applyOpponentMove(pos: Int) {
        val s = _state.value
        val enemy = if (s.myMark == "X") "O" else "X"

        if (pos !in 0..8) return

        val board = s.board.toMutableList()
        board[pos] = enemy

        viewModelScope.launch {
            when (val result = evaluateBoardUseCase(board)) {
                is Resource.Data -> {
                    when (val res = result.value) {
                        is GameResult.Win -> {
                            endGame(board, res.winner, res.winningLine)
                        }
                        GameResult.Draw -> drawGame(board)
                        GameResult.Ongoing -> _state.update {
                            it.copy(
                                board = board,
                                isMyTurn = true,
                                statusText = "Your turn"
                            )
                        }
                    }
                }
                is Resource.Error -> {}
            }
        }
    }

    // ---------------- RESET ----------------
    fun resetGame(receivedFromOpponent: Boolean = false) {
        val current = _state.value

        if (!receivedFromOpponent) {
            viewModelScope.launch {
                runCatching {
                    sendMoveUseCase(RESET_CODE)
                }
            }
        }

        _state.update {
            it.copy(
                board = List(9) { "" },
                gameOver = false,
                winner = null,
                winningLine = null,
                isMyTurn = current.myMark == "X",
                statusText = if (current.myMark == "X") "Your turn" else "Opponent's turn"
            )
        }
    }

    // ---------------- END GAME ----------------
    private fun endGame(board: List<String>, winner: String, line: List<Int>) {
        val current = _state.value

        _state.update {
            it.copy(
                board = board,
                gameOver = true,
                winner = winner,
                winningLine = line,
                statusText =
                    if (winner == current.myMark) "🎉 You Win!"
                    else "😢 Opponent Wins!"
            )
        }
    }

    private fun drawGame(board: List<String>) {
        _state.update {
            it.copy(
                board = board,
                gameOver = true,
                winner = null,
                winningLine = null,
                statusText = "🤝 Draw!"
            )
        }
    }
}