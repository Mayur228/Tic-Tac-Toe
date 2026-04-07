package com.demo.tictactoe.framework

import com.buildwithmayur.bluetooth.api.BluetoothPeer
import com.buildwithmayur.bluetooth.api.ClassicBluetoothSdk
import com.buildwithmayur.bluetooth.api.ConnectionState
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.ConnectionModel
import com.demo.tictactoe.core.common.model.DataModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.model.ServerModel
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BluetoothApiImpl @Inject constructor(
    private val sdk: ClassicBluetoothSdk
) : BluetoothApi {

    // Map SDK connection state to Core module state
    override val connectionState: Flow<BluetoothConnectionState> =
        sdk.connectionState.map { it.toCoreState() }

    // Discover devices as a Flow
    override suspend fun discoverServers(serverName: String): Flow<DeviceModel> =
        sdk.scanHostDevices(serverName = serverName).map { peer ->
            peer.toDeviceModel()
        }

    // Start hosting a server
    override suspend fun startServer(serverName: String): ServerModel {
        val result = sdk.startServer(serverName).getOrThrow() // unwrap Result
        return ServerModel(
            serverName = result.serverName,
            isServerStart = result.isServerStart
        )
    }

    // Connect to a device
    override suspend fun connect(device: DeviceModel): ConnectionModel {
        val peer = BluetoothPeer(
            name = device.name,
            address = device.address
        )
        val result = sdk.connect(peer).getOrThrow()

        return ConnectionModel(
            peer = result.peer.toDeviceModel(),
            isConnected = result.isConnected
        )
    }


    // Send move as ByteArray
    override suspend fun sendMove(move: Int): DataModel {
        val result = sdk.send(byteArrayOf(move.toByte())).getOrThrow()

        return DataModel(
            bytesSent = result.bytesSent,
            success = result.success
        )
    }

    // Observe incoming moves
    override val incomingMoves: Flow<Int> =
        sdk.observeIncoming().map { it.firstOrNull()?.toInt() ?: 0 } // assuming first byte = move

    // Disconnect
    override suspend fun disconnect() {
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
