package com.example.proyectofinaldisenomovil.core.component.moderator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.proyectofinaldisenomovil.core.theme.*

/**
 * Logout confirmation dialog matching the "PopUp logout" mockup.
 *
 * Shows a question with the moderator's name and offers two actions:
 *  - "Seguir en línea" (green)  — dismisses and keeps the session active.
 *  - "Cerrar sesion"  (red)     — triggers sign-out via [onConfirmLogout].
 *
 * @param moderatorName    Full name of the logged-in moderator shown in the question.
 * @param isLoading        Shows a spinner on the logout button while sign-out is in progress.
 * @param onConfirmLogout  Called when the user confirms they want to sign out.
 * @param onDismiss        Called when ✕ or "Seguir en línea" is tapped.
 */
@Composable
fun LogoutDialog(
    moderatorName: String,
    isLoading: Boolean = false,
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // ── ✕ close button — top-left ─────────────────────────────────
                IconButton(
                    onClick  = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp),
                ) {
                    Icon(
                        imageVector        = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint               = MaterialTheme.colorScheme.background,
                        modifier           = Modifier.size(22.dp),
                    )
                }

                // ── Dialog content ────────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 48.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Question
                    Text(
                        text      = "¿Está seguro de cerrar sesion de la cuenta de moderador de $moderatorName?",
                        color     = MaterialTheme.colorScheme.onSurface,
                        fontSize  = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                    )

                    Spacer(Modifier.height(24.dp))

                    // ── Buttons row ───────────────────────────────────────────
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        // "Seguir en línea" — green
                        Button(
                            onClick  = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape  = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        ) {
                            Icon(
                                imageVector        = Icons.Default.WifiTethering,
                                contentDescription = null,
                                modifier           = Modifier.size(15.dp),
                                tint               = Color.White,
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text       = "Seguir en linea",
                                fontSize   = 12.sp,
                                color      = Color.White,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }

                        // "Cerrar sesion" — red
                        Button(
                            onClick  = onConfirmLogout,
                            enabled  = !isLoading,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            shape  = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color       = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier    = Modifier.size(16.dp),
                                )
                            } else {
                                Icon(
                                    imageVector        = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                    modifier           = Modifier.size(15.dp),
                                    tint               = Color.White,
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text       = "Cerrar sesion",
                                    fontSize   = 12.sp,
                                    color      = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogoutDialogPreview() {
    ProyectoFinalDisenoMovilTheme() {
        LogoutDialog(
            moderatorName = "Juan Pérez",
            onConfirmLogout = {},
            onDismiss = {},
        )
    }
}