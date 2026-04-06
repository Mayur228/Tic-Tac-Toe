package com.demo.tictactoe.ui.scan

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.ui.gamehost.ConnectionState
import com.demo.tictactoe.ui.gamehost.GameViewModel

@SuppressLint("MissingPermission")
@Composable
fun ScanScreen(
    onConnected: () -> Unit
) {
    val viewModel: ScanViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    // 🔥 Trigger scan once
    LaunchedEffect(Unit) {
        viewModel.startScanForHost()
    }

    // 🔥 Handle navigation
    LaunchedEffect(state) {
        if (state is ScanState.Success) {
            val success = state as ScanState.Success
            if (success.startGame) {
                onConnected()
            }
        }
    }

    // Animation (UNCHANGED)
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B))
            .padding(20.dp)
    ) {

        // -------- HEADER --------
        ScanHeader(pulse)

        Spacer(modifier = Modifier.height(20.dp))

        when (state) {

            // ---------------- LOADING ----------------
            is ScanState.Loading -> {

            }

            // ---------------- SUCCESS ----------------
            is ScanState.Success -> {

                val success = state as ScanState.Success

                // 🔥 Connection State UI
                ConnectionStatus(success.connectionState)

                Spacer(modifier = Modifier.height(16.dp))

                if (success.discoveredDevices.isEmpty()) {
                    Text(
                        text = "No devices found yet...",
                        fontSize = 16.sp,
                        color = Color(0xFF7A8AAE),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(success.discoveredDevices) { device ->

                        DeviceItem(
                            device = device,
                            onClick = { viewModel.connect(device) }
                        )
                    }
                }
            }

            is ScanState.Error -> {

            }
        }
    }
}

@Composable
private fun ScanHeader(pulse: Float) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size((60 * pulse).dp)
                .background(
                    Color(0xFF4C9EFF).copy(alpha = 0.25f),
                    RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size((35 * pulse).dp)
                    .background(Color(0xFF4C9EFF), RoundedCornerShape(50.dp))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Searching for Host...",
            fontSize = 24.sp,
            color = Color.White
        )

        Text(
            text = "Make sure host device is discoverable",
            fontSize = 14.sp,
            color = Color(0xFF8EA7C1)
        )
    }
}

@Composable
private fun DeviceItem(
    device: DeviceModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F1A30)
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            Text(
                text = device.name ?: "Unknown Device",
                color = Color(0xFF4C9EFF),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = device.address,
                color = Color(0xFF7C90AE),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ConnectionStatus(state: ConnectionState) {

    val text = when (state) {
        ConnectionState.Scanning -> "Scanning devices..."
        ConnectionState.Connecting -> "Connecting..."
        ConnectionState.Connected -> "Connected ✅"
        ConnectionState.Failed -> "Connection Failed ❌"
        ConnectionState.Idle -> "Idle"
        ConnectionState.Advertising -> "Advertising"
        ConnectionState.Cancelled -> "Cancelled"
    }

    val color = when (state) {
        ConnectionState.Connected -> Color.Green
        ConnectionState.Failed -> Color.Red
        else -> Color(0xFF8EA7C1)
    }

    Text(
        text = text,
        color = color,
        fontSize = 14.sp
    )
}