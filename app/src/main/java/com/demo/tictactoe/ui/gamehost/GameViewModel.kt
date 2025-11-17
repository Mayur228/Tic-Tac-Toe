package com.demo.tictactoe.ui.gamehost

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.bluetooth_sdk.sdk.ConnectionState
import com.demo.bluetooth_sdk.sdk.GameBluetoothSdk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val sdk: GameBluetoothSdk
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    init {
        // Observe SDK connection state
        viewModelScope.launch {
            sdk.connectionState.collect { connection ->
                when (connection) {
                    ConnectionState.Default -> updateStatus("Default", ConnectionState.Default)
                    ConnectionState.Scanning -> updateStatus("Scanning...", ConnectionState.Scanning)
                    ConnectionState.Waiting -> updateStatus("Waiting for player...", ConnectionState.Waiting)
                    ConnectionState.Connecting -> updateStatus("Connecting...", ConnectionState.Connecting)
                    is ConnectionState.Connected -> updateStatus("Connected with ${connection.deviceName}", connection)
                    ConnectionState.Failed -> updateStatus("Connection failed", ConnectionState.Failed)
                }
            }
        }

        // Observe moves
        viewModelScope.launch {
            sdk.moveFlow.collect { move ->
                if (move == RESET_CODE) resetGame(receivedFromOpponent = true)
                else applyOpponentMove(move)
            }
        }

        // Observe reset flow
        viewModelScope.launch {
            sdk.resetFlow.collect { resetGame(receivedFromOpponent = true) }
        }

        // Observe discovered host names
        viewModelScope.launch {
            sdk.discoveredHosts.collect { hosts ->
                _state.update { it.copy(discoveredDevices = hosts) }
            }
        }
    }

    companion object {
        private const val RESET_CODE = 99
    }

    // ------------------------ HOST GAME ------------------------
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun hostGame(hostName: String) {
        sdk.hostGame(hostName)
    }

    // ------------------------ JOIN / SCAN ------------------------
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun startScan() {
        sdk.startScanForHosts()
    }

    fun joinGame(hostName: String) {
        sdk.joinGame(hostName)
    }

    // ------------------------ MAKE MOVE ------------------------
    fun makeMove(pos: Int) {
        val s = _state.value
        if (s.gameOver || !s.isMyTurn || s.board[pos].isNotEmpty() || s.connectionState !is ConnectionState.Connected) return

        sdk.makeMove(pos)
        applyLocalMove(pos)
    }

    // ------------------------ RESET ------------------------
    fun resetGame(receivedFromOpponent: Boolean = false) {
        if (!receivedFromOpponent) sdk.resetGame()

        _state.update { st ->
            st.copy(
                board = List(9) { "" },
                gameOver = false,
                winner = null,
                winningLine = null,
                isMyTurn = st.myMark == "X",
                statusText = if (st.myMark == "X") "Your turn" else "Opponent's turn"
            )
        }
    }

    // ------------------------ LOCAL MOVE ------------------------
    private fun applyLocalMove(pos: Int) {
        val st = _state.value
        val board = st.board.toMutableList()
        board[pos] = st.myMark

        val win = checkWinner(board)
        val draw = isDraw(board)

        when {
            win != null -> endGame(board, st.myMark, win)
            draw -> drawGame(board)
            else -> _state.update { it.copy(board = board, isMyTurn = false, statusText = "Waiting for opponent...") }
        }
    }

    // ------------------------ OPPONENT MOVE ------------------------
    private fun applyOpponentMove(pos: Int) {
        val st = _state.value
        val enemy = if (st.myMark == "X") "O" else "X"

        val board = st.board.toMutableList()
        if (pos < 0 || pos >= board.size) return
        board[pos] = enemy

        val win = checkWinner(board)
        val draw = isDraw(board)

        when {
            win != null -> endGame(board, enemy, win)
            draw -> drawGame(board)
            else -> _state.update { it.copy(board = board, isMyTurn = true, statusText = "Your turn") }
        }
    }

    // ------------------------ END / DRAW ------------------------
    private fun endGame(board: List<String>, winner: String, line: List<Int>) {
        _state.update {
            it.copy(
                board = board,
                gameOver = true,
                winner = winner,
                winningLine = line,
                statusText = if (winner == it.myMark) "🎉 You Win!" else "😢 Opponent Wins!"
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

    // ------------------------ WIN CHECKER ------------------------
    private fun checkWinner(board: List<String>): List<Int>? {
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
            if (board[a].isNotEmpty() && board[a] == board[b] && board[b] == board[c]) return line
        }
        return null
    }

    private fun isDraw(board: List<String>): Boolean = board.all { it.isNotEmpty() } && checkWinner(board) == null

    // ------------------------ STATUS UPDATE ------------------------
    private fun updateStatus(text: String, connection: ConnectionState) {
        _state.update { it.copy(statusText = text, connectionState = connection) }
    }

    // ------------------------ CLEANUP ------------------------
    override fun onCleared() {
        super.onCleared()
        sdk.clear() // cancels all SDK coroutines
    }
}