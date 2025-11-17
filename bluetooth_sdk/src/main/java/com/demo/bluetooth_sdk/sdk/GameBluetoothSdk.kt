package com.demo.bluetooth_sdk.sdk

import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import com.demo.bluetooth_sdk.domain.usecase.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.core.annotation.Factory

@Factory
class GameBluetoothSdk(
    private val startScanUseCase: StartScanUseCase,
    private val hostGameUseCase: HostGameUseCase,
    private val joinGameUseCase: JoinGameUseCase,
    private val sendMoveUseCase: SendMoveUseCase,
    private val observeMovesUseCase: ObserveMovesUseCase
) {

    private val sdkScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _connectionState = MutableSharedFlow<ConnectionState>(replay = 1)
    val connectionState: SharedFlow<ConnectionState> = _connectionState.asSharedFlow()

    // Filtered host names only
    private val _discoveredHosts = MutableStateFlow<List<String>>(emptyList())
    val discoveredHosts: StateFlow<List<String>> = _discoveredHosts.asStateFlow()

    private val discoveredDevices = mutableListOf<BluetoothDevice>()
    private val _resetFlow = MutableSharedFlow<Unit>()
    val resetFlow: SharedFlow<Unit> = _resetFlow.asSharedFlow()

    val moveFlow: Flow<Int> = observeMovesUseCase()
        .onEach { if (it == RESET_CODE) _resetFlow.emit(Unit) }

    companion object {
        private const val RESET_CODE = 99
    }

    private var hostNamePrefix: String = "Tic Tac Toe Host"

    // ----------------------------------------------------------------
    // HOST GAME with custom host name
    // ----------------------------------------------------------------
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun hostGame(customHostName: String) {
        hostNamePrefix = customHostName
        sdkScope.launch { _connectionState.emit(ConnectionState.Waiting) }

        sdkScope.launch {
            val name = try { hostGameUseCase(customHostName) } catch (_: Throwable) { null }
            if (name != null) _connectionState.emit(ConnectionState.Connected(name))
            else _connectionState.emit(ConnectionState.Failed)
        }
    }

    // ----------------------------------------------------------------
    // JOIN GAME - SCAN for hosts matching prefix
    // ----------------------------------------------------------------
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun startScanForHosts() {
        sdkScope.launch { _connectionState.emit(ConnectionState.Scanning) }

        sdkScope.launch {
            startScanUseCase().collect { device ->
                val name = device.name ?: return@collect
                // Filter host devices by prefix automatically
                if (!name.startsWith(hostNamePrefix, ignoreCase = true)) return@collect

                synchronized(discoveredDevices) {
                    if (discoveredDevices.none { it.address == device.address }) {
                        discoveredDevices.add(device)
                        _discoveredHosts.update { it + name }
                    }
                }
            }
        }
    }

    fun joinGame(hostName: String) {
        sdkScope.launch { _connectionState.emit(ConnectionState.Connecting) }

        sdkScope.launch {
            val targetDevice = synchronized(discoveredDevices) {
                discoveredDevices.firstOrNull { it.name == hostName }
            }

            if (targetDevice == null) {
                _connectionState.emit(ConnectionState.Failed)
                return@launch
            }

            val success = try { joinGameUseCase(targetDevice) } catch (_: Throwable) { false }
            if (success) _connectionState.emit(ConnectionState.Connected(hostName))
            else _connectionState.emit(ConnectionState.Failed)
        }
    }

    // ----------------------------------------------------------------
    // SEND MOVE
    // ----------------------------------------------------------------
    fun makeMove(pos: Int) {
        sdkScope.launch { try { sendMoveUseCase(pos) } catch (_: Throwable) {} }
    }

    // ----------------------------------------------------------------
    // RESET GAME
    // ----------------------------------------------------------------
    fun resetGame() {
        sdkScope.launch { sendMoveUseCase(RESET_CODE) }
    }

    // ----------------------------------------------------------------
    // CLEANUP
    // ----------------------------------------------------------------
    fun clear() { sdkScope.cancel() }
}
