package com.example.proyectofinaldisenomovil.features.moderatorFlow.panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.moderator.CategoryFilterChips
import com.example.proyectofinaldisenomovil.core.component.moderator.LogoutDialog
import com.example.proyectofinaldisenomovil.core.component.moderator.ModeratorEventCard
import com.example.proyectofinaldisenomovil.core.component.moderator.ModeratorTopBar
import com.example.proyectofinaldisenomovil.core.component.moderator.SortDistanceRow
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun ModeratorPanelScreen(
    onEventClick: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: ModeratorPanelViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Top Bar
        ModeratorTopBar(
            navController = navController,
            title = "Moderación de eventos",
            searchQuery = uiState.searchQuery,
            onSearchChange = viewModel::onSearchQueryChange,
            onLogoutClick = viewModel::onLogoutClick,
        )

        // Category Filter Chips
        CategoryFilterChips(
            selectedCategory = uiState.selectedCategory,
            onCategorySelect = viewModel::onCategorySelect,
        )

        // Sort and Distance Row
        SortDistanceRow(
            selectedSort = uiState.sortBy,
            selectedDistance = uiState.distanceKm,
            onSortChange = viewModel::onSortChange,
            onDistanceChange = viewModel::onDistanceChange,
        )

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                uiState.filteredEvents.isEmpty() -> {
                    EmptyState(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 16.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(
                            items = uiState.filteredEvents,
                            key = { it.id },
                        ) { event ->
                            ModeratorEventCard(
                                navController = navController,
                                event = event,
                                onCardClick = { onEventClick(event.id) },
                                onAccept = viewModel::onEventAccept,
                                onReject = viewModel::onEventReject,
                            )
                        }
                    }
                }
            }
        }
    }

    // Logout Dialog
    if (uiState.showLogoutDialog) {
        LogoutDialog(
            moderatorName = "Moderador",
            onConfirmLogout = {
                viewModel.onLogoutConfirm()
                onLogout()
            },
            onDismiss = viewModel::onLogoutDismiss,
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.EventBusy,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No hay eventos pendientes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Los eventos pendientes de revisión aparecerán aquí",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ModeratorPanelScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        ModeratorPanelScreen(
            onEventClick = {},
            onLogout = {},
        )
    }
}
