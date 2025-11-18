package com.demo.tictactoe.core.common.network

import com.demo.tictactoe.core.common.model.DeviceModel

interface BluetoothApi {
    suspend fun createServer(serverName: String): Unit
    suspend fun discoverServer(serverName: String): List<DeviceModel>
    suspend fun connectToServer(server: String): Boolean
    suspend fun sendDataToServer(pos: Int): Unit
    suspend fun receiveDataFromServer(): Int

}