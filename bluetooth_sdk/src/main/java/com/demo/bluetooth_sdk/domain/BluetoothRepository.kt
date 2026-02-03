package com.demo.bluetooth_sdk.domain

import android.bluetooth.BluetoothDevice
import com.demo.bluetooth_sdk.api.BluetoothPeer
import com.demo.bluetooth_sdk.api.ConnectionState
import kotlinx.coroutines.flow.Flow
public interface BluetoothRepository {

    public val connectionState: Flow<ConnectionState>
    public val incomingData: Flow<Int>

    public fun discoverAllDevices(): Flow<BluetoothPeer>
    public fun discoverHostDevices(serverName: String): Flow<BluetoothPeer>

    public suspend fun startServer(serverName: String): Result<Unit>
    public suspend fun connect(address: String): Result<Unit>

    public fun findBondedPeer(address: String): BluetoothPeer?

    public suspend fun send(data: Int): Result<Unit>
    public fun disconnect()
}



