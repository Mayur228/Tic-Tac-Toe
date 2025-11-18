package com.demo.tictactoe.ui.gamehost

import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.bluetooth_sdk.sdk.ClassicBluetoothManager
import com.demo.tictactoe.core.feature.host.domain.usecase.HostGameUseCase
import com.demo.tictactoe.core.feature.host.domain.usecase.JoinGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val hostGameUseCase: HostGameUseCase,
    private val joinGameUseCase: JoinGameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    /*init {
        // register a client-connected callback from SDK (if SDK exposes it)
        try {
            bluetoothManager.onClientConnected {
                _state.update {
                    it.copy(connectionState = ConnectionState.Connected, statusText = "Connected")
                }
            }
        } catch (_: Throwable) {
            // ignore if SDK does not support callback
        }

        viewModelScope.launch {
            bluetoothManager.incomingData.collect { move ->
                // special reset code
                if (move == RESET_CODE) {
                    resetGame(receivedFromOpponent = true)
                    return@collect
                }

                // When first move arrives -> mark connected
                if (_state.value.connectionState != ConnectionState.Connected) {
                    _state.update { s -> s.copy(connectionState = ConnectionState.Connected, statusText = "Connected") }
                }

                applyOpponentMove(move)
            }
        }
    }*/

    companion object {
        // reserved code for reset sync
        private const val RESET_CODE = 99
        // host advertised name prefix
        const val HOST_NAME_PREFIX = "Tic Tac Toe Host"
    }

    // ----------------------------------------------------------------
    // HOST
    // ----------------------------------------------------------------
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
            // waitForPlayer returns remote name or null
            val name = try {
                hostGameUseCase("Tic Tac Toe Host")
            } catch (t: Throwable) {
                null
            }

            if (name != null) {
                _state.update {
                    it.copy(connectionState = ConnectionState.Connected, statusText = "Connected with $name")
                }
            } else {
                _state.update { it.copy(connectionState = ConnectionState.Failed, statusText = "Failed to accept player") }
            }
        }
    }

    // ----------------------------------------------------------------
    // JOIN GAME (scan)
    // ----------------------------------------------------------------
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
            /*bluetoothManager.startScan("Tic Tac Toe Host").collectLatest { device ->
                _state.update { s ->
                    s.copy(discoveredDevices = s.discoveredDevices + device)

                }
            }*/

            val device = joinGameUseCase.invoke("Tic Tac Toe Host")

            _state.update { s ->
                s.copy(discoveredDevices = device)
            }
        }
    }

    // ----------------------------------------------------------------
    // CONNECT
    // ----------------------------------------------------------------
   /* fun connect(device: BluetoothDevice) {
        _state.update { it.copy(connectionState = ConnectionState.Connecting, statusText = "Connecting...") }

        viewModelScope.launch {
            val success = try {
                bluetoothManager.connectToDevice(device)
            } catch (t: Throwable) {
                false
            }

            if (success) {
                _state.update { it.copy(connectionState = ConnectionState.Connected, statusText = "Connected!") }
            } else {
                _state.update { it.copy(connectionState = ConnectionState.Failed, statusText = "Connection failed") }
            }
        }
    }*/

    // ----------------------------------------------------------------
    // MAKE MOVE
    // ----------------------------------------------------------------
    /*fun makeMove(pos: Int) {
        val s = _state.value
        if (s.gameOver || !s.isMyTurn || s.board[pos].isNotEmpty() || s.connectionState != ConnectionState.Connected) return

        viewModelScope.launch {
            try {
                bluetoothManager.sendData(pos)
            } catch (_: Throwable) {}
            applyLocalMove(pos)
        }
    }*/

    // ----------------------------------------------------------------
    // LOCAL MOVE
    // ----------------------------------------------------------------
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

    // ----------------------------------------------------------------
    // OPPONENT MOVE
    // ----------------------------------------------------------------
    private fun applyOpponentMove(pos: Int) {
        val st = _state.value
        val enemy = if (st.myMark == "X") "O" else "X"

        val board = st.board.toMutableList()
        // guard
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

    // ----------------------------------------------------------------
    // SYNC RESET
    // ----------------------------------------------------------------
   /* fun resetGame(receivedFromOpponent: Boolean = false) {
        val mark = state.value.myMark

        if (!receivedFromOpponent) {
            // send reset signal to opponent
            viewModelScope.launch {
                try {
                    bluetoothManager.sendData(RESET_CODE)
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
    }*/

    // ----------------------------------------------------------------
    // GAME END
    // ----------------------------------------------------------------
    private fun endGame(board: List<String>, winner: String, line: List<Int>) {
        _state.update {
            it.copy(board = board, gameOver = true, winner = winner, winningLine = line, statusText = if (winner == it.myMark) "🎉 You Win!" else "😢 Opponent Wins!")
        }
    }

    private fun drawGame(board: List<String>) {
        _state.update {
            it.copy(board = board, gameOver = true, winner = null, winningLine = null, statusText = "🤝 Draw!")
        }
    }

    // ----------------------------------------------------------------
    // WIN CHECKER
    // ----------------------------------------------------------------
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
}