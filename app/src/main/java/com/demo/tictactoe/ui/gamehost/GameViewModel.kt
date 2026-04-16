package com.demo.tictactoe.ui.gamehost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.tictactoe.common.GameConfig.HOST_NAME_PREFIX
import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.ServerModel
import com.demo.tictactoe.core.feature.host.domain.usecase.HostGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val hostGameUseCase: HostGameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<HostState>(HostState.Loading)
    val state = _state.asStateFlow()

    private var isStarted = false

    fun startHosting() {
        if (isStarted) return
        isStarted = true

        viewModelScope.launch {

            // ✅ Immediately show waiting state
            _state.value = HostState.Success(
                statusText = "Waiting for player...",
                connectionState = ConnectionState.Advertising
            )

            val result = hostGameUseCase("TicTacToe")

            when (result) {
                is Resource.Data<ServerModel> -> {
                    _state.value = HostState.Success(
                        connectionState = ConnectionState.Connected,
                        statusText = "Player connected!"
                    )
                }

                is Resource.Error -> {
                    _state.value = HostState.Error(
                        message = result.throwable.message ?: "Unknown Error"
                    )
                }
            }
        }
    }

    fun cancelConnection() {
        isStarted = false
        _state.value = HostState.Loading
        // TODO: call Bluetooth disconnect
    }
}
