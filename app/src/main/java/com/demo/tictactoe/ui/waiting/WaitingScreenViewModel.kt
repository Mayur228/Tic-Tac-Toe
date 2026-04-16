package com.demo.tictactoe.ui.waiting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.feature.waiting.domain.usecase.CheckGameStatusUseCase
import com.demo.tictactoe.ui.gamehost.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingScreenViewModel @Inject constructor(
    private val checkGameStatusUseCase: CheckGameStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<WaitingState>(WaitingState())
    val state = _state.asStateFlow()

    init {
        observeConnection() // ✅ AUTO START
    }

    private fun observeConnection() {
        viewModelScope.launch {
            checkGameStatusUseCase().collect { data ->

                val mappedState = when (data) {
                    is BluetoothConnectionState.Connected ->
                        ConnectionState.Connected

                    BluetoothConnectionState.Connecting ->
                        ConnectionState.Connecting

                    BluetoothConnectionState.Scanning ->
                        ConnectionState.Scanning

                    BluetoothConnectionState.Idle ->
                        ConnectionState.Idle

                    BluetoothConnectionState.Disconnected ->
                        ConnectionState.Failed

                    is BluetoothConnectionState.Error ->
                        ConnectionState.Failed
                }

                _state.value = WaitingState(
                    connectionState = mappedState,
                    message = mappedState.name
                )
            }
        }
    }

    fun cancelConnection() {
        // TODO: call BluetoothApi.disconnect()
    }
}