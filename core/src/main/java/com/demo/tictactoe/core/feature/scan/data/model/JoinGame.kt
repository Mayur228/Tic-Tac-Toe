package com.demo.tictactoe.core.feature.scan.data.model

import com.demo.tictactoe.core.common.model.DeviceModel

data class JoinGame(
    val serverName: String,
    val device: DeviceModel
)