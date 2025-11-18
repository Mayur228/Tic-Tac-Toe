package com.demo.tictactoe.framework

import android.bluetooth.BluetoothDevice
import com.demo.bluetooth_sdk.sdk.ClassicBluetoothManager
import com.demo.tictactoe.FTAClass
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.network.BluetoothApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class BluetoothApiImpl @Inject constructor(
    val manager: ClassicBluetoothManager,
    val activityProvider: FTAClass
) : BluetoothApi {

    override suspend fun createServer(serverName: String) {
        manager.hostServer(serverName)
    }

    override suspend fun discoverServer(serverName: String): List<DeviceModel> {
        // collect first matching device and return
        val devices = mutableListOf<DeviceModel>()
        val flow: Flow<BluetoothDevice> = manager.startScan(serverName)
        flow.collect { device ->
            devices.add(DeviceModel(name = device.name, address = device.address))
        }
        return devices
    }

    override suspend fun connectToServer(server: String): Boolean {
        return manager.connectToDevice(server as BluetoothDevice)
    }

    override suspend fun sendDataToServer(pos: Int) {
        manager.sendData(pos)
    }

    override suspend fun receiveDataFromServer(): Int {
        // collect first value from flow
        return manager.receiveData().firstOrNull()?.toInt() ?: 0
    }
}
