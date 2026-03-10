package com.example.proyectofinaldisenomovil.core.component.AlertDialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmAlertDialog(
    titulo : String,
    mensaje : String,
    onShowExitDialogChange: (Boolean) -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        title = { Text(text = titulo) },
        text = { Text(text = mensaje) },
        onDismissRequest = { onShowExitDialogChange(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    onShowExitDialogChange(false)
                    onConfirm()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onShowExitDialogChange(false) }
            ) {
                Text("Cerrar")
            }
        }
    )
}