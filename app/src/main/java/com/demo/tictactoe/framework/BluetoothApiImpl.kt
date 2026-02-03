package com.demo.tictactoe.framework

import com.demo.bluetooth_sdk.api.ClassicBluetoothSdk
import com.demo.bluetooth_sdk.api.ConnectionState
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
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

    override fun discoverServers(serverName: String): Flow<DeviceModel> =
        sdk.scanHostDevices(serverName).map {
            DeviceModel(
                name = it.name ?: "Unknown",
                address = it.address
            )
        }

    override suspend fun startServer(serverName: String) {
        sdk.startServer(serverName)
    }

    override suspend fun connect(device: DeviceModel) {
        val peer = sdk.findBondedPeer(device.address) ?: error("Device not found")
        sdk.connect(peer)
    }

    override suspend fun sendMove(move: Int) {
        sdk.send(move)
    }

    override val incomingMoves: Flow<Int> = sdk.observeIncoming()

    override fun disconnect() {
        sdk.disconnect()
    }

    // -----------------------------
    // Mapping extension from SDK -> Core
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
}
