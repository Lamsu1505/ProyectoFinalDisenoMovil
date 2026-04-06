package com.example.proyectofinaldisenomovil.core.component.moderator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinaldisenomovil.core.theme.*

/**
 * Reusable green TopBar used across all moderator screens.
 *
 * Two display modes controlled by [showBackArrow]:
 *
 * **Panel mode** (showBackArrow = false):
 *   • Title centered
 *   • Search field below the title row
 *   • Logout icon on the right
 *
 * **Detail mode** (showBackArrow = true):
 *   • Back arrow on the left
 *   • Title centered
 *   • Logout icon on the right
 *   • No search field
 *
 * @param title           Text displayed as the screen title.
 * @param showBackArrow   When true renders the back arrow and hides the search bar.
 * @param searchQuery     Current text in the search field (panel mode only).
 * @param onSearchChange  Called on every keystroke in the search field.
 * @param onSearchDone    Called when the keyboard "Done" action fires.
 * @param onBackClick     Called when the back arrow is tapped (detail mode).
 * @param onLogoutClick   Called when the logout icon is tapped — triggers the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeratorTopBar(
    title: String,
    modifier: Modifier = Modifier,
    showBackArrow: Boolean = false,
    searchQuery: String = "",
    onSearchChange: (String) -> Unit = {},
    onSearchDone: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color    = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(
                start  = 12.dp,
                end    = 12.dp,
                top    = 12.dp,
                bottom = if (showBackArrow) 12.dp else 10.dp,
            )
        ) {
            // ── Title row ─────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center,
            ) {
                // Back arrow (detail mode only)
                if (showBackArrow) {
                    IconButton(
                        onClick  = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint               = Color.White,
                        )
                    }
                }

                // Centered title
                Text(
                    text       = title,
                    color      = Color.White,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.align(Alignment.Center),
                )

                // Logout icon — always visible
                IconButton(
                    onClick  = onLogoutClick,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesion",
                        tint               = Color.White,
                    )
                }
            }

            // ── Search bar (panel mode only) ──────────────────────────────────
            if (!showBackArrow) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value         = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder   = {
                        Text(
                            text     = "Buscar Eventos....",
                            color    = Color.Gray,
                            fontSize = 14.sp,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector        = androidx.compose.material.icons.Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint               = Color.Gray,
                            modifier           = Modifier.size(20.dp),
                        )
                    },
                    singleLine    = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearchDone() }),
                    shape  = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor      = Color.Transparent,
                        unfocusedBorderColor    = Color.Transparent,
                        cursorColor             = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                )
            }
        }
    }
}
