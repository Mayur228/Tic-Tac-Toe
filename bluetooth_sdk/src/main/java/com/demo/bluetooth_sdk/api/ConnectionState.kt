package com.demo.bluetooth_sdk.api

public sealed class ConnectionState {

    public object Idle : ConnectionState()

    public object Scanning : ConnectionState()

    public object Connecting : ConnectionState()

    public data class Connected(val deviceName: String?) : ConnectionState()

    public object Disconnected : ConnectionState()

    public data class Error(val error: BluetoothError) : ConnectionState()
}
