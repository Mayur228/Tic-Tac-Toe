package com.demo.tictactoe.ui.gamehost

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.tictactoe.core.feature.game.data.model.GameResult
import com.demo.tictactoe.core.feature.host.domain.usecase.HostGameUseCase
import com.demo.tictactoe.ui.AiDifficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val hostGameUseCase: HostGameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    companion object {
        const val RESET_CODE = 99
        const val HOST_NAME_PREFIX = "Tic Tac Toe Host"
    }



    // ------------------------------------------------------------
    // HOST GAME
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
            val result = try {
                hostGameUseCase(HOST_NAME_PREFIX)
            } catch (t: Throwable) {
                null
            }

            if (result != null) {
                _state.update {
                    it.copy(
                        connectionState = ConnectionState.Connected,
                        showFirstMoveDialog = true,
                        isFirstMoveDecided = false,
                        statusText = "Connected with $result"
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


}
