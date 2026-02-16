package com.demo.tictactoe.framework

import com.buildwithmayur.bluetooth.api.ClassicBluetoothSdk
import com.buildwithmayur.bluetooth.api.ConnectionState
import com.buildwithmayur.bluetooth.api.BluetoothPeer
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BluetoothApiImpl @Inject constructor(
    private val sdk: ClassicBluetoothSdk
) : BluetoothApi {

    // Map SDK connection state to Core module state
    override val connectionState: Flow<BluetoothConnectionState> =
        sdk.connectionState.map { it.toCoreState() }

    // Discover devices as a Flow
    override fun discoverServers(serverName: String): Flow<DeviceModel> =
        sdk.scanDevices().map { peer ->
            peer.toDeviceModel()
        }

    // Start hosting a server
    override suspend fun startServer(serverName: String) {
        sdk.startServer(serverName).getOrThrow() // unwrap Result
    }

    // Connect to a device
    override suspend fun connect(device: DeviceModel) {
        val peer = sdk.scanDevices() // we need the peer from scan
            .map { it } // mapping placeholder
            // In real case, you should find matching peer by address
            // But SDK doesn't provide direct getByAddress, so you might store scanned peers somewhere
            // Example: peers.first { it.address == device.address }
            .first() // placeholder
        sdk.connect(peer).getOrThrow()
    }

    // Send move as ByteArray
    override suspend fun sendMove(move: Int) {
        sdk.send(byteArrayOf(move.toByte())).getOrThrow()
    }

    // Observe incoming moves
    override val incomingMoves: Flow<Int> =
        sdk.observeIncoming().map { it.firstOrNull()?.toInt() ?: 0 } // assuming first byte = move

    // Disconnect
    override fun disconnect() {
        sdk.disconnect()
    }

    // -----------------------------
    // Mapping extensions
    // -----------------------------
    private fun ConnectionState.toCoreState(): BluetoothConnectionState =
        when (this) {
            is ConnectionState.Idle -> BluetoothConnectionState.Idle
            is ConnectionState.Scanning -> BluetoothConnectionState.Scanning
            is ConnectionState.Connecting -> BluetoothConnectionState.Connecting
            is ConnectionState.Connected -> BluetoothConnectionState.Connected(this.deviceName)
            is ConnectionState.Disconnected -> BluetoothConnectionState.Disconnected
            is ConnectionState.Error -> BluetoothConnectionState.Error(this.error.toString())
        }

    private fun BluetoothPeer.toDeviceModel() = DeviceModel(
        name = this.name ?: "Unknown",
        address = this.address
    )
}
