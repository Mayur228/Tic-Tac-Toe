package com.demo.bluetooth_sdk.internal.repository

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.annotation.RequiresPermission
import com.demo.bluetooth_sdk.api.BluetoothPeer
import com.demo.bluetooth_sdk.api.ConnectionState
import com.demo.bluetooth_sdk.api.BluetoothError
import com.demo.bluetooth_sdk.api.BluetoothException
import com.demo.bluetooth_sdk.domain.BluetoothRepository
import com.demo.bluetooth_sdk.internal.*

import com.demo.bluetooth_sdk.internal.permission.BluetoothPermissionChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

internal class BluetoothRepositoryImpl(
    private val context: Context,
    private val adapter: BluetoothAdapter?
) : BluetoothRepository {

    private val stateFlow = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    override val connectionState: Flow<ConnectionState> = stateFlow

    private val socketHolder = BluetoothSocketHolder()
    private val incomingFlow = MutableSharedFlow<Int>()
    override val incomingData: Flow<Int> = incomingFlow

    private val permissionChecker = BluetoothPermissionChecker(context)
    private val scanner = BluetoothScanner(context, adapter)
    private val client = BluetoothClient(context, adapter, socketHolder, incomingFlow, stateFlow)
    private val server = BluetoothServer(context, adapter, socketHolder, incomingFlow, stateFlow)

    override fun discoverAllDevices(): Flow<BluetoothPeer> {
        permissionChecker.checkScanPermission()
        stateFlow.value = ConnectionState.Scanning
        return scanner.scanAll()
    }

    override fun discoverHostDevices(serverName: String): Flow<BluetoothPeer> {
        permissionChecker.checkScanPermission()
        return scanner.scanHost(serverName)
    }

    override suspend fun connect(address: String): Result<Unit> {
        permissionChecker.checkConnectPermission()
        val device = adapter?.bondedDevices?.firstOrNull { it.address == address }
            ?: return Result.failure(BluetoothException.DeviceNotFound)
        return client.connect(device)
    }

    override suspend fun startServer(serverName: String): Result<Unit> {
        permissionChecker.checkAdvertisePermission()
        return server.start(serverName)
    }

    override suspend fun send(data: Int): Result<Unit> = client.send(data)

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun findBondedPeer(address: String): BluetoothPeer? {
        val device = adapter?.bondedDevices?.firstOrNull { it.address == address } ?: return null
        return BluetoothPeer(name = device.name, address = device.address)
    }

    override fun disconnect() {
        socketHolder.close()
        stateFlow.value = ConnectionState.Disconnected
    }
}
