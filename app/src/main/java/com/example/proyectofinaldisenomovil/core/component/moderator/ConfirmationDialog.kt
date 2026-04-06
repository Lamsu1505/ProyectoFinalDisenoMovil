package com.example.proyectofinaldisenomovil.core.component.moderator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.proyectofinaldisenomovil.core.theme.*

/**
 * Fully reusable confirmation dialog matching the "MENSAJE DE CONFIRMACIÓN" mockup.
 *
 * Can be used for:
 *  - Rejecting an event (with a mandatory reason text field)
 *  - Any destructive action that needs explicit confirmation
 *
 * @param title              Dialog title in green caps (e.g. "MENSAJE DE CONFIRMACIÓN").
 * @param bodyText           Question shown below the title.
 * @param showReasonField    When true, renders the "Motivos para rechazar" text area.
 * @param reasonValue        Current text in the reason field.
 * @param onReasonChange     Called on every keystroke in the reason field.
 * @param reasonError        Inline error shown below the reason field when null check fails.
 * @param reasonLabel        Label above the text area (default: "Motivos para rechazar el evento *").
 * @param reasonPlaceholder  Placeholder inside the text area.
 * @param confirmLabel       Text on the green confirm button (default: "Rechazar").
 * @param cancelLabel        Text on the orange cancel button (default: "Cancelar").
 * @param isLoading          Shows a spinner in the confirm button during async operations.
 * @param onConfirm          Called when the confirm button is tapped.
 * @param onDismiss          Called when the ✕ or cancel button is tapped.
 */
@Composable
fun ConfirmationDialog(
    title: String = "MENSAJE DE CONFIRMACIÓN",
    bodyText: String,
    showReasonField: Boolean = false,
    reasonValue: String = "",
    onReasonChange: (String) -> Unit = {},
    reasonError: String? = null,
    reasonLabel: String = "Motivos para rechazar el evento *",
    reasonPlaceholder: String = "Escriba aqui sus motivos...",
    confirmLabel: String = "Rechazar",
    cancelLabel: String = "Cancelar",
    isLoading: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 44.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Title
                    Text(
                        text       = title,
                        color      = MaterialTheme.colorScheme.primary,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign  = TextAlign.Center,
                        lineHeight = 26.sp,
                    )

                    Spacer(Modifier.height(12.dp))

                    // Body question
                    Text(
                        text      = bodyText,
                        color     = MaterialTheme.colorScheme.onSurface,
                        fontSize  = 14.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 20.sp,
                        modifier  = Modifier.fillMaxWidth(),
                    )

                    // ── Reason text area (optional) ───────────────────────────
                    if (showReasonField) {
                        Spacer(Modifier.height(16.dp))

                        Text(
                            text       = reasonLabel,
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color      = MaterialTheme.colorScheme.onSurface,
                            modifier   = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(6.dp))

                        OutlinedTextField(
                            value         = reasonValue,
                            onValueChange = onReasonChange,
                            placeholder   = {
                                Text(reasonPlaceholder, color = MaterialTheme.colorScheme.onBackground, fontSize = 13.sp)
                            },
                            isError       = reasonError != null,
                            minLines      = 4,
                            maxLines      = 6,
                            shape         = RoundedCornerShape(8.dp),
                            colors        = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor   = MaterialTheme.colorScheme.primary,
                                errorBorderColor     = MaterialTheme.colorScheme.tertiary,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )

                        if (reasonError != null) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text     = reasonError,
                                color    = MaterialTheme.colorScheme.tertiary,
                                fontSize = 11.sp,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Action buttons row ────────────────────────────────────
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        // Cancel button (orange)
                        OutlinedButton(
                            onClick  = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape  = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor   = Color.White,
                            ),
                            border = null,
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Replay,
                                contentDescription = null,
                                modifier           = Modifier.size(16.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(cancelLabel, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        // Confirm button (green)
                        Button(
                            onClick  = onConfirm,
                            enabled  = !isLoading,
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape  = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color       = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier    = Modifier.size(18.dp),
                                )
                            } else {
                                Icon(
                                    imageVector        = Icons.Default.Save,
                                    contentDescription = null,
                                    modifier           = Modifier.size(16.dp),
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(confirmLabel, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}