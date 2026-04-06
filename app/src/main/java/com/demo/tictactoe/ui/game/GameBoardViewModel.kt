package com.demo.tictactoe.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.tictactoe.core.feature.game.data.model.GameResult
import com.demo.tictactoe.core.feature.game.domain.usecase.EvaluateBoardUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.ObserveMovesUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.SendMoveUseCase
import com.demo.tictactoe.ui.AiDifficulty
import com.demo.tictactoe.ui.gamehost.ConnectionState
import com.demo.tictactoe.ui.gamehost.GameState
import com.demo.tictactoe.ui.gamehost.GameViewModel.Companion.RESET_CODE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameBoardViewModel @Inject constructor(
    private val observeMovesUseCase: ObserveMovesUseCase,
    private val sendMoveUseCase: SendMoveUseCase,
    private val evaluateBoardUseCase: EvaluateBoardUseCase,
): ViewModel() {


    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    init {
        observeIncomingMoves()
    }

    // ------------------------------------------------------------
    // OBSERVE OPPONENT MOVES
    // ------------------------------------------------------------
    private var observeJob: Job? = null

    private fun observeIncomingMoves() {

        observeJob?.cancel()

        observeJob = viewModelScope.launch {
            observeMovesUseCase().collect { move ->

                val currentState = _state.value

                if (currentState.isSinglePlayer) return@collect
                if (currentState.connectionState != ConnectionState.Connected) return@collect

                if (move == RESET_CODE) {
                    resetGame(receivedFromOpponent = true)
                } else {
                    applyOpponentMove(move)
                }
            }
        }
    }

    fun setDifficulty(difficulty: AiDifficulty) {
        _state.update { it.copy(aiDifficulty = difficulty) }
    }

    // ------------------------------------------------------------
    // SINGLE PLAYER
    // ------------------------------------------------------------
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

        // If AI mode and AI starts → trigger AI move
        if (_state.value.isSinglePlayer && !isMeFirst) {
            triggerAiIfNeeded()
        }
    }

    // ------------------------------------------------------------
    // MAKE MOVE
    // ------------------------------------------------------------
    fun makeMove(pos: Int) {
        val s = _state.value

        if (s.gameOver) return
        if (!s.isMyTurn) return
        if (s.board[pos].isNotEmpty()) return

        if (s.isSinglePlayer) {
            applyLocalMove(pos)

            // After player move → trigger AI
            triggerAiIfNeeded()
        } else {
            if (s.connectionState != ConnectionState.Connected) return

            viewModelScope.launch {
                try {
                    sendMoveUseCase(pos)
                    applyLocalMove(pos)
                } catch (_: Throwable) {}
            }
        }
    }

    private fun triggerAiIfNeeded() {

        val state = _state.value

        // 🔒 SAFETY CHECKS
        if (!state.isSinglePlayer) return
        if (state.gameOver) return
        if (state.isMyTurn) return   // Only AI plays when it's NOT my turn

        viewModelScope.launch {

            delay(500) // AI thinking delay

            val latest = _state.value
            if (latest.gameOver) return@launch
            if (latest.isMyTurn) return@launch

            val board = latest.board

            val aiMove = when (latest.aiDifficulty) {
                AiDifficulty.EASY -> getRandomMove(board)
                AiDifficulty.MEDIUM -> getStrategicMove(board)
                AiDifficulty.HARD -> getBestMoveMinimax(board)
            }

            applyOpponentMove(aiMove)
        }
    }


    private fun makeAiMoveIfNeeded() {
        val state = _state.value
        if (state.gameOver) return

        val board = state.board

        viewModelScope.launch {
            delay(400)

            val aiMove = when (state.aiDifficulty) {
                AiDifficulty.EASY -> getRandomMove(board)
                AiDifficulty.MEDIUM -> getStrategicMove(board)
                AiDifficulty.HARD -> getBestMoveMinimax(board)
            }

            applyOpponentMove(aiMove)
        }
    }

    private fun getRandomMove(board: List<String>): Int {
        val empty = board.indices.filter { board[it].isEmpty() }
        return empty.random()
    }


    private fun getStrategicMove(board: List<String>): Int {

        val ai = if (_state.value.myMark == "X") "O" else "X"
        val player = _state.value.myMark

        // 1️⃣ Try to win
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                val copy = board.toMutableList()
                copy[i] = ai
                if (evaluateBoardUseCase(copy) is GameResult.Win) return i
            }
        }

        // 2️⃣ Try to block player
        for (i in board.indices) {
            if (board[i].isEmpty()) {
                val copy = board.toMutableList()
                copy[i] = player
                if (evaluateBoardUseCase(copy) is GameResult.Win) return i
            }
        }

        // 3️⃣ Otherwise random
        return getRandomMove(board)
    }


    private fun getBestMoveMinimax(board: List<String>): Int {

        val ai = if (_state.value.myMark == "X") "O" else "X"
        var bestScore = Int.MIN_VALUE
        var move = -1

        for (i in board.indices) {
            if (board[i].isEmpty()) {
                val copy = board.toMutableList()
                copy[i] = ai
                val score = minimax(copy, false)
                if (score > bestScore) {
                    bestScore = score
                    move = i
                }
            }
        }

        return move
    }

    private fun minimax(board: MutableList<String>, isMaximizing: Boolean): Int {

        when (val result = evaluateBoardUseCase(board)) {
            is GameResult.Win -> {
                val ai = if (_state.value.myMark == "X") "O" else "X"
                return if (result.winner == ai) 10 else -10
            }
            GameResult.Draw -> return 0
            GameResult.Ongoing -> {}
        }

        val ai = if (_state.value.myMark == "X") "O" else "X"
        val player = _state.value.myMark

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in board.indices) {
                if (board[i].isEmpty()) {
                    board[i] = ai
                    val score = minimax(board, false)
                    board[i] = ""
                    bestScore = maxOf(score, bestScore)
                }
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in board.indices) {
                if (board[i].isEmpty()) {
                    board[i] = player
                    val score = minimax(board, true)
                    board[i] = ""
                    bestScore = minOf(score, bestScore)
                }
            }
            return bestScore
        }
    }


    // ------------------------------------------------------------
    // APPLY LOCAL MOVE
    // ------------------------------------------------------------
    private fun applyLocalMove(pos: Int) {
        val st = _state.value
        val board = st.board.toMutableList()
        board[pos] = st.myMark

        when (val result = evaluateBoardUseCase(board)) {
            is GameResult.Win -> endGame(board, result.winner, result.winningLine)
            GameResult.Draw -> drawGame(board)
            GameResult.Ongoing -> _state.update {
                it.copy(
                    board = board,
                    isMyTurn = false,
                    statusText = "Waiting for opponent..."
                )
            }
        }
    }

    // ------------------------------------------------------------
    // APPLY OPPONENT MOVE
    // ------------------------------------------------------------
    private fun applyOpponentMove(pos: Int) {
        val st = _state.value
        val enemy = if (st.myMark == "X") "O" else "X"

        if (pos !in 0..8) return

        val board = st.board.toMutableList()
        board[pos] = enemy

        when (val result = evaluateBoardUseCase(board)) {
            is GameResult.Win -> endGame(board, result.winner, result.winningLine)
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

    // ------------------------------------------------------------
    // RESET GAME
    // ------------------------------------------------------------
    fun resetGame(receivedFromOpponent: Boolean = false) {

        val mark = state.value.myMark

        if (!receivedFromOpponent) {
            viewModelScope.launch {
                try { sendMoveUseCase(RESET_CODE) } catch (_: Throwable) {}
            }
        }

        _state.update {
            it.copy(
                board = List(9) { "" },
                gameOver = false,
                winner = null,
                winningLine = null,
                isMyTurn = mark == "X",
                statusText = if (mark == "X") "Your turn" else "Opponent's turn"
            )
        }
    }



    // ------------------------------------------------------------
    // END GAME
    // ------------------------------------------------------------
    private fun endGame(board: List<String>, winner: String, line: List<Int>) {
        _state.update {
            it.copy(
                board = board,
                gameOver = true,
                winner = winner,
                winningLine = line,
                statusText =
                    if (winner == it.myMark)
                        "🎉 You Win!"
                    else
                        "😢 Opponent Wins!"
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