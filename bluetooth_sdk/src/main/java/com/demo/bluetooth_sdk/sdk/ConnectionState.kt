package com.demo.bluetooth_sdk.sdk

sealed class ConnectionState {
    object Default : ConnectionState()
    object Scanning : ConnectionState()
    object Waiting : ConnectionState()
    object Connecting : ConnectionState()
    object Failed : ConnectionState()
    data class Connected(val deviceName: String) : ConnectionState()
}
