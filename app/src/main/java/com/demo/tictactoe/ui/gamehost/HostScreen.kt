package com.demo.tictactoe.ui.host

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.tictactoe.common.BlePermissionHelper
import com.demo.tictactoe.ui.gamehost.GameViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun HostScreen(
    viewModel: GameViewModel,
    onConnected: () -> Unit
) {

    val activity = LocalContext.current as Activity
    var hostName by remember { mutableStateOf("") }
    var isWaiting by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->

        val allGranted = result.values.all { it }
        if (!allGranted) return@rememberLauncherForActivityResult

        if (!BlePermissionHelper.isGpsEnabled(activity)) {
            activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return@rememberLauncherForActivityResult
        }

        val discoverIntent =
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            }

        activity.startActivity(discoverIntent)
        viewModel.hostGame(hostName)
        isWaiting = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07102B))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Host Game",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(30.dp))

        if (!isWaiting) {

            OutlinedTextField(
                value = hostName,
                onValueChange = { hostName = it },
                label = { Text("Enter Your Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    if (hostName.isNotBlank()) {
                        permissionLauncher.launch(
                            BlePermissionHelper.requiredPermissions()
                        )
                    }
                },
                modifier = Modifier
                    .width(230.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4C9EFF),
                    contentColor = Color.White
                )
            ) {
                Text("Start Hosting", fontSize = 18.sp)
            }

        } else {

            Text(
                text = "Waiting for player...",
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(Modifier.height(20.dp))

            CircularProgressIndicator(color = Color.White)
        }
    }
}