package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun AppSnackbarHost(
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->

            Snackbar(
                snackbarData = snackbarData,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                actionColor = Color.White
            )
        }
    )
}