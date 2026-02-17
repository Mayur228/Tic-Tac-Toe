package com.demo.tictactoe.ui.gamehost

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.game.data.model.GameResult
import com.demo.tictactoe.core.feature.game.domain.usecase.ConnectToGameUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.EvaluateBoardUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.ObserveMovesUseCase
import com.demo.tictactoe.core.feature.game.domain.usecase.SendMoveUseCase
import com.demo.tictactoe.core.feature.host.domain.usecase.HostGameUseCase
import com.demo.tictactoe.core.feature.host.domain.usecase.JoinGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val hostGameUseCase: HostGameUseCase,
    private val joinGameUseCase: JoinGameUseCase,
    private val connectToGameUseCase: ConnectToGameUseCase,
    private val observeMovesUseCase: ObserveMovesUseCase,
    private val sendMoveUseCase: SendMoveUseCase,
    private val evaluateBoardUseCase: EvaluateBoardUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    companion object {
        private const val RESET_CODE = 99
        const val HOST_NAME_PREFIX = "Tic Tac Toe Host"
    }

    init {
        observeIncomingMoves()
    }

    // ------------------------------------------------------------
    // Observe Opponent Moves
    // ------------------------------------------------------------
    private fun observeIncomingMoves() {
        viewModelScope.launch {
            observeMovesUseCase().collect { move ->
                if (move == RESET_CODE) {
                    resetGame(receivedFromOpponent = true)
                } else {
                    applyOpponentMove(move)
                }
            }
        }
    }

    // ------------------------------------------------------------
    // HOST
    // ------------------------------------------------------------
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun hostGame() {

        _state.update {
            it.copy(
                myMark = "X",
                isMyTurn = true,
                statusText = "Host is Waiting for player",
                connectionState = ConnectionState.Advertising
            )
        }

        viewModelScope.launch {
            val name = try {
                hostGameUseCase(HOST_NAME_PREFIX)
            } catch (t: Throwable) {
                null
            }

            if (name != null) {
                _state.update {
                    it.copy(
                        connectionState = ConnectionState.Connected,
                        statusText = "Connected with $name"
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        connectionState = ConnectionState.Failed,
                        statusText = "Failed to accept player"
                    )
                }
            }
        }
    }

    // ------------------------------------------------------------
    // JOIN (SCAN)
    // ------------------------------------------------------------
    fun joinGame() {
        _state.update {
            it.copy(
                myMark = "O",
                isMyTurn = false,
                statusText = "Scanning…",
                connectionState = ConnectionState.Scanning,
                discoveredDevices = emptyList()
            )
        }

        viewModelScope.launch {
            joinGameUseCase(HOST_NAME_PREFIX).collect { device ->
                _state.update { s ->
                    if (s.discoveredDevices.none { it.address == device.address }) {
                        s.copy(discoveredDevices = s.discoveredDevices + device)
                    } else s
                }
            }
        }
    }

    // ------------------------------------------------------------
    // CONNECT TO SELECTED DEVICE
    // ------------------------------------------------------------
    fun connect(device: DeviceModel) {
        _state.update {
            it.copy(
                connectionState = ConnectionState.Connecting,
                statusText = "Connecting..."
            )
        }

        viewModelScope.launch {
            try {
                connectToGameUseCase(device)
                _state.update {
                    it.copy(
                        connectionState = ConnectionState.Connected,
                        statusText = "Connected!"
                    )
                }
            } catch (t: Throwable) {
                _state.update {
                    it.copy(
                        connectionState = ConnectionState.Failed,
                        statusText = "Connection failed"
                    )
                }
            }
        }
    }

    // ------------------------------------------------------------
    // MAKE MOVE
    // ------------------------------------------------------------
    fun makeMove(pos: Int) {
        val s = _state.value

        if (s.gameOver ||
            !s.isMyTurn ||
            s.board[pos].isNotEmpty() ||
            s.connectionState != ConnectionState.Connected
        ) return

        viewModelScope.launch {
            try {
                sendMoveUseCase(pos)
                applyLocalMove(pos)
            } catch (_: Throwable) { }
        }
    }

    // ------------------------------------------------------------
    // LOCAL MOVE
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
    // OPPONENT MOVE
    // ------------------------------------------------------------
    private fun applyOpponentMove(pos: Int) {
        val st = _state.value
        val enemy = if (st.myMark == "X") "O" else "X"

        if (pos < 0 || pos >= st.board.size) return

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
    // RESET GAME (SYNCED)
    // ------------------------------------------------------------
    fun resetGame(receivedFromOpponent: Boolean = false) {
        val mark = state.value.myMark

        if (!receivedFromOpponent) {
            viewModelScope.launch {
                try {
                    sendMoveUseCase(RESET_CODE)
                } catch (_: Throwable) {}
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
                statusText = if (winner == it.myMark)
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
