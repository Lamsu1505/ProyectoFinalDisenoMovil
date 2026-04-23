package com.example.proyectofinaldisenomovil.core.component.moderator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    navController: NavController,
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
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(
                start  = 16.dp,
                end    = 16.dp,
                top    = 12.dp,
                bottom = if (showBackArrow) 24.dp else 20.dp,
            )
        ) {
            // ── Title row ─────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
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
                            tint               = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }

                // Centered title
                Text(
                    text       = title,
                    color      = MaterialTheme.colorScheme.onPrimary,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.align(Alignment.Center),
                )

                // Logout icon — always visible
                IconButton(
                    onClick  = onLogoutClick,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar sesion",
                        tint               = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            // ── Search bar (panel mode only) ──────────────────────────────────
            if (!showBackArrow) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value         = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder   = {
                        Text(
                            text     = "Buscar Eventos....",
                            color    = MaterialTheme.colorScheme.outline,
                            fontSize = 15.sp,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector        = androidx.compose.material.icons.Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint               = MaterialTheme.colorScheme.outline,
                            modifier           = Modifier.size(24.dp),
                        )
                    },
                    singleLine    = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearchDone() }),
                    shape  = RoundedCornerShape(25.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedBorderColor      = Color.Transparent,
                        unfocusedBorderColor    = Color.Transparent,
                        cursorColor             = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModeratorTopBarPreview() {
    ProyectoFinalDisenoMovilTheme() {
        ModeratorTopBar (
            navController = rememberNavController(),
            title = "Moderación de eventos",
            onLogoutClick = {},
        )
    }
}
