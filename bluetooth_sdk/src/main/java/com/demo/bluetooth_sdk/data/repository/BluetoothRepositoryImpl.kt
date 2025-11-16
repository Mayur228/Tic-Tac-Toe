package com.demo.bluetooth_sdk.data.repository

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import com.demo.bluetooth_sdk.data.BluetoothClient
import com.demo.bluetooth_sdk.data.BluetoothScanner
import com.demo.bluetooth_sdk.data.BluetoothServer
import com.demo.bluetooth_sdk.domain.repository.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.annotation.Single

@Single
class BluetoothRepositoryImpl(
    private val context: Context,
    private val adapter: BluetoothAdapter?
) : BluetoothRepository {

    // central shared socket
    private var sharedSocket: BluetoothSocket? = null

    // flow for moves
    private val _incomingMoves = MutableSharedFlow<Int>()
    override val incomingMoves = _incomingMoves

    // callback to notify host immediately when socket accepted
    var onClientConnectedCallback: (() -> Unit)? = null

    // lazily created client/server using same socket ref
    private val client by lazy {
        BluetoothClient(
            context = context,
            adapter = adapter,
            incomingMoves = _incomingMoves,
            sharedSocketRef = { sharedSocket },
            setSocket = { socket -> sharedSocket = socket }
        )
    }

    private val server by lazy {
        BluetoothServer(
            adapter = adapter,
            incomingMoves = _incomingMoves,
            setSocket = { socket -> sharedSocket = socket },
            onClientConnected = {
                // call back to ViewModel via usecase/SDK
                onClientConnectedCallback?.invoke()
            }
        )
    }

    override fun startScan() = BluetoothScanner(context, adapter).startScan()

    override suspend fun startServer() = server.waitForPlayer()

    override suspend fun connectTo(device: BluetoothDevice) = client.connectTo(device)

    override suspend fun sendMove(move: Int) = client.sendMove(move)

    override fun setOnClientConnected(cb: () -> Unit) {
        onClientConnectedCallback = cb
    }

}