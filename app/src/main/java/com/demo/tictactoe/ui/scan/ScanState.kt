package com.demo.tictactoe.ui.scan

import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.ui.gamehost.ConnectionState

sealed class ScanState {
    object Loading: ScanState()
    data class Success(
        val discoveredDevices: List<DeviceModel> = emptyList(),
        val connectionState: ConnectionState = ConnectionState.Idle,
        val startGame: Boolean,
    ): ScanState()
    data class Error(val message: String): ScanState()
}
