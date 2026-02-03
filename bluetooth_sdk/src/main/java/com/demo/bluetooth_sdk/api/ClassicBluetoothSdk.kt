package com.demo.bluetooth_sdk.api

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.demo.bluetooth_sdk.domain.BluetoothRepository
import com.demo.bluetooth_sdk.internal.repository.BluetoothRepositoryImpl
import kotlinx.coroutines.flow.Flow

public class ClassicBluetoothSdk private constructor(
    private val repository: BluetoothRepository
) {

    /** Observe connection lifecycle */
    public val connectionState: Flow<ConnectionState>
        get() = repository.connectionState

    /** Scan all nearby devices */
    public fun scanDevices(): Flow<BluetoothPeer> =
        repository.discoverAllDevices()

    /** Scan hosts matching a server name */
    public fun scanHostDevices(serverName: String): Flow<BluetoothPeer> =
        repository.discoverHostDevices(serverName)

    /** Start hosting */
    public suspend fun startServer(serverName: String): Result<Unit> =
        repository.startServer(serverName)

    /** Connect using peer info (NOT BluetoothDevice) */
    public suspend fun connect(peer: BluetoothPeer): Result<Unit> =
        repository.connect(peer.address)

    /** Send small data payload */
    public suspend fun send(data: Int): Result<Unit> =
        repository.send(data)

    /** Observe incoming data */
    public fun observeIncoming(): Flow<Int> =
        repository.incomingData

    /** Find already bonded device (by address) */
    public fun findBondedPeer(address: String): BluetoothPeer? =
        repository.findBondedPeer(address)

    /** Disconnect safely */
    public fun disconnect() {
        repository.disconnect()
    }

    public companion object {
        public fun initialize(context: Context): ClassicBluetoothSdk {
            val adapter = BluetoothAdapter.getDefaultAdapter()
                ?: throw BluetoothNotSupportedException()

            return ClassicBluetoothSdk(
                BluetoothRepositoryImpl(
                    context = context.applicationContext,
                    adapter = adapter
                )
            )
        }
    }
}


/**
 * Exception thrown when device does not support Bluetooth
 */
public class BluetoothNotSupportedException :
    Exception("Bluetooth is not supported on this device")
