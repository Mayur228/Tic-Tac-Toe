package com.demo.tictactoe.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.tictactoe.common.GameConfig.HOST_NAME_PREFIX
import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.ConnectionModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.feature.scan.domain.usecase.JoinUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.ScanHostUseCase
import com.demo.tictactoe.core.feature.scan.domain.usecase.StopConnectionUseCase
import com.demo.tictactoe.ui.gamehost.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanHostUseCase: ScanHostUseCase,
    private val connectToGameUseCase: JoinUseCase,
    private val stopConnectionUseCase: StopConnectionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ScanState>(ScanState.Loading)
    val state = _state.asStateFlow()
    private var scanJob: Job? = null
    private var timeoutJob: Job? = null

    // ------------------------------------------------------------
    // TIMEOUT HANDLER (60 seconds)
    // ------------------------------------------------------------
    private fun startTimeout() {

        timeoutJob?.cancel()

        timeoutJob = viewModelScope.launch {
            delay(60_000L)

            val current = _state.value
            if (current is ScanState.Success &&
                current.connectionState != ConnectionState.Connected
            ) {
                cancelConnection()

                _state.value = ScanState.Success(
                    discoveredDevices = current.discoveredDevices,
                    connectionState = ConnectionState.Failed,
                    startGame = false
                )
            }
        }
    }

    fun startScanForHost() {
        viewModelScope.launch {
            val result = scanHostUseCase.invoke(HOST_NAME_PREFIX)

            when(result) {
                is Resource.Data<Flow<DeviceModel>> -> {
                    result.value.collect {
                        _state.value = ScanState.Success(
                            discoveredDevices = listOf(it),
                            connectionState = ConnectionState.Scanning,
                            startGame = false
                        )
                    }

                }
                is Resource.Error -> {

                }
            }
        }
    }


    // ------------------------------------------------------------
    // CONNECT TO SELECTED DEVICE
    // ------------------------------------------------------------
    fun connect(device: DeviceModel) {

        _state.value = ScanState.Loading

        viewModelScope.launch {
            val result = connectToGameUseCase.invoke(device)

            when(result){
                is Resource.Data<ConnectionModel> -> {
                    timeoutJob?.cancel()
                    scanJob?.cancel()

                    _state.value = ScanState.Success(
                        discoveredDevices = listOf(result.value.peer),
                        connectionState = ConnectionState.Connected,
                        startGame = result.value.isConnected
                    )
                }
                is Resource.Error -> {

                }
            }
        }
    }

    // ------------------------------------------------------------
    // CANCEL CONNECTION
    // ------------------------------------------------------------
    fun cancelConnection() {

        timeoutJob?.cancel()
        scanJob?.cancel()

        viewModelScope.launch {
            stopConnectionUseCase()
            val current = _state.value as ScanState.Success

            _state.value = ScanState.Success(
                discoveredDevices = current.discoveredDevices,
                connectionState = ConnectionState.Failed,
                startGame = false
            )
        }
    }
}