package com.example.qrscanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun QRScanner(navController: NavController) {
    var result by remember {
        mutableStateOf("")
    }
    var qrScanned by remember {
        mutableStateOf(false)
    }

    val qrCode = remember { mutableStateOf<Barcode?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraXPreview(onBarcodesDetected = { barcodes ->
            qrCode.value = barcodes.firstOrNull()
            result = qrCode.value?.rawValue ?: ""
            qrScanned = true
        })

        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close QR scanner")
        }
    }

    if (qrScanned) {
        LaunchedEffect(result) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("qrCode", result)
            navController.popBackStack()
        }
    }
}
