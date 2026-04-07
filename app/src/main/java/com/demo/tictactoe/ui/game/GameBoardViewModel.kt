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
import com.demo.tictactoe.ui.gamehost.ConnectionState
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
) : ViewModel() {


    private val _state = MutableStateFlow<GameBoardState>(GameBoardState.Loading)
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

                val currentState = _state.value as GameBoardState.GameData

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
        _state.value = GameBoardState.GameData(aiDifficulty = difficulty)
    }

    // ------------------------------------------------------------
    // SINGLE PLAYER
    // ------------------------------------------------------------
    fun startSinglePlayer() {
        _state.value = GameBoardState.GameData(
            isSinglePlayer = true,
            showDifficultyDialog = true
        )
    }

    fun onDifficultySelected(difficulty: AiDifficulty) {
        _state.value = GameBoardState.GameData(
            aiDifficulty = difficulty,
            showDifficultyDialog = false,
            showFirstMoveDialog = true

        )
    }

    fun selectFirstPlayer(isMeFirst: Boolean) {

        val myMark = if (isMeFirst) "X" else "O"

        val currentState = _state.value as GameBoardState.GameData

        currentState.copy(
            myMark = myMark,
            isMyTurn = isMeFirst,
            showFirstMoveDialog = false,
            isFirstMoveDecided = true,
            statusText = if (isMeFirst) "Your turn" else "Opponent's turn"
        )

        // If AI mode and AI starts → trigger AI move
        if (currentState.isSinglePlayer && !isMeFirst) {
            //triggerAiIfNeeded()
        }
    }

    // ------------------------------------------------------------
    // MAKE MOVE
    // ------------------------------------------------------------
    fun makeMove(pos: Int) {
        val s = _state.value as GameBoardState.GameData

        if (s.gameOver) return
        if (!s.isMyTurn) return
        if (s.board[pos].isNotEmpty()) return

        if (s.isSinglePlayer) {
            applyLocalMove(pos)

            // After player move → trigger AI
           // triggerAiIfNeeded()
        } else {
            if (s.connectionState != ConnectionState.Connected) return

            viewModelScope.launch {
                try {
                    sendMoveUseCase(pos)
                    applyLocalMove(pos)
                } catch (_: Throwable) {
                }
            }
        }
    }

    /*private fun triggerAiIfNeeded() {

        val state = _state.value as GameBoardState.GameData

        // 🔒 SAFETY CHECKS
        if (!state.isSinglePlayer) return
        if (state.gameOver) return
        if (state.isMyTurn) return   // Only AI plays when it's NOT my turn

        viewModelScope.launch {

            delay(500) // AI thinking delay

            val latest = _state.value as GameBoardState.GameData
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
    }*/


    /*private fun makeAiMoveIfNeeded() {
        val state = _state.value as GameBoardState.GameData
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

    private fun getStrategicMove(board: List<String>): Int {

        val currentState = _state.value as GameBoardState.GameData
        val ai = if (currentState.myMark == "X") "O" else "X"
        val player = currentState.myMark

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
        val currentState = _state.value as GameBoardState.GameData

        val ai = if (currentState.myMark == "X") "O" else "X"
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
        val currentState = _state.value as GameBoardState.GameData

        viewModelScope.launch {
            when (val result = evaluateBoardUseCase(board)) {

                is Resource.Data<GameResult> -> {
                    when(result.value){
                        GameResult.Draw -> {
                             0
                        }
                        GameResult.Ongoing -> {

                        }
                        is GameResult.Win -> {
                            val ai = if (currentState.myMark == "X") "O" else "X"
                             if ((result as GameResult.Win).winner == ai) 10 else -10
                        }
                    }
                }
                is Resource.Error -> {

                }
            }
        }


        val ai = if (currentState.myMark == "X") "O" else "X"
        val player = currentState.myMark

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
*/

    // ------------------------------------------------------------
    // APPLY LOCAL MOVE
    // ------------------------------------------------------------
    private fun applyLocalMove(pos: Int) {
        val st = _state.value as GameBoardState.GameData
        val board = st.board.toMutableList()
        board[pos] = st.myMark

        viewModelScope.launch {
            when (val result = evaluateBoardUseCase(board)) {
                is Resource.Data<GameResult> -> {
                    when(result.value){
                        GameResult.Draw -> drawGame(board)
                        GameResult.Ongoing -> _state.value = st.copy(
                            board = board,
                            isMyTurn = false,
                            statusText = "Waiting for opponent..."
                        )
                        is GameResult.Win -> {
                            endGame(board, (result.value as GameResult.Win).winner, (result.value as GameResult.Win).winningLine)
                        }
                    }
                }
                is Resource.Error -> {

                }
            }
        }
    }

    // ------------------------------------------------------------
    // APPLY OPPONENT MOVE
    // ------------------------------------------------------------
    private fun applyOpponentMove(pos: Int) {
        val st = _state.value as GameBoardState.GameData
        val enemy = if (st.myMark == "X") "O" else "X"

        if (pos !in 0..8) return

        val board = st.board.toMutableList()
        board[pos] = enemy

        viewModelScope.launch {
            when (val result = evaluateBoardUseCase(board)) {
                is Resource.Data<GameResult> -> {
                    when(result.value){
                        is GameResult.Win -> {
                            endGame(board, (result.value as GameResult.Win).winner, (result.value as GameResult.Win).winningLine)
                        }
                        GameResult.Draw -> drawGame(board)
                        GameResult.Ongoing -> _state.value = st.copy(
                            board = board,
                            isMyTurn = true,
                            statusText = "Your turn"
                        )
                    }
                }
                is Resource.Error -> {}
            }
        }

    }

    // ------------------------------------------------------------
    // RESET GAME
    // ------------------------------------------------------------
    fun resetGame(receivedFromOpponent: Boolean = false) {
        val currentState = _state.value as GameBoardState.GameData

        if (!receivedFromOpponent) {
            viewModelScope.launch {
                try {
                    sendMoveUseCase(RESET_CODE)
                } catch (_: Throwable) {
                }
            }
        }

        _state.update {
            currentState.copy(
                board = List(9) { "" },
                gameOver = false,
                winner = null,
                winningLine = null,
                isMyTurn = currentState.myMark == "X",
                statusText = if (currentState.myMark == "X") "Your turn" else "Opponent's turn"
            )
        }
    }


    // ------------------------------------------------------------
    // END GAME
    // ------------------------------------------------------------
    private fun endGame(board: List<String>, winner: String, line: List<Int>) {
        val currentState = _state.value as GameBoardState.GameData
        _state.update {
            currentState.copy(
                board = board,
                gameOver = true,
                winner = winner,
                winningLine = line,
                statusText =
                    if (winner == currentState.myMark)
                        "🎉 You Win!"
                    else
                        "😢 Opponent Wins!"
            )
        }
    }

    private fun drawGame(board: List<String>) {
        val currentState = _state.value as GameBoardState.GameData
        _state.update {
            currentState.copy(
                board = board,
                gameOver = true,
                winner = null,
                winningLine = null,
                statusText = "🤝 Draw!"
            )
        }
    }
}