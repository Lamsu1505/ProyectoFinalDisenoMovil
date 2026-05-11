package com.example.proyectofinaldisenomovil.features.moderatorFlow.panel

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.map.MapboxViewer
import com.example.proyectofinaldisenomovil.core.component.moderator.CategoryFilterChips
import com.example.proyectofinaldisenomovil.core.component.moderator.LogoutDialog
import com.example.proyectofinaldisenomovil.core.component.moderator.ModeratorEventCard
import com.example.proyectofinaldisenomovil.core.component.moderator.ModeratorTopBar
import com.example.proyectofinaldisenomovil.core.component.moderator.SortDistanceRow
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import com.example.proyectofinaldisenomovil.domain.model.Location
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.moderator.ConfirmationDialog
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository.printEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeratorPanelScreen(
    onEventClick: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: ModeratorPanelViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var showMapSheet by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }
    
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    if (showMapSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMapSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(16.dp)
            ) {
                MapboxViewer(
                    events = uiState.filteredEvents,
                    userLocation = uiState.userLocation,
                    onUserLocationObtained = { lat, lon ->
                        viewModel.updateUserLocation(lat, lon)
                    },
                    onEventClick = { eventId ->
                        showMapSheet = false
                        onEventClick(eventId)
                    }
                )
                
                FloatingActionButton(
                    onClick = {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Mi ubicación"
                    )
                }
                
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .clickable { showMapSheet = false },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = stringResource(R.string.moderator_map_close),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
    
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadEvents()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ModeratorTopBar(
            navController = navController,
            title = stringResource(R.string.moderator_title),
            searchQuery = uiState.searchQuery,
            onSearchChange = viewModel::onSearchQueryChange,
            onLogoutClick = viewModel::onLogoutClick,
        )

        CategoryFilterChips(
            selectedCategory = uiState.selectedCategory,
            onCategorySelect = viewModel::onCategorySelect,
        )

        Spacer(modifier = Modifier.size(7.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusChip(
                label = "Todos (${uiState.events.size})",
                isSelected = uiState.statusFilter == null,
                color = MaterialTheme.colorScheme.primary,
                onClick = { viewModel.onStatusFilterChange(null) }
            )
            StatusChip(
                label = "Pendientes (${uiState.pendingEvents.size})",
                isSelected = uiState.statusFilter == EventStatus.PENDING_REVIEW,
                color = Color(0xFFFFA000),
                onClick = { viewModel.onStatusFilterChange(EventStatus.PENDING_REVIEW) }
            )
            StatusChip(
                label = "Verificados (${uiState.verifiedEvents.size})",
                isSelected = uiState.statusFilter == EventStatus.VERIFIED,
                color = Color(0xFF4CAF50),
                onClick = { viewModel.onStatusFilterChange(EventStatus.VERIFIED) }
            )
            StatusChip(
                label = "Rechazados (${uiState.rejectedEvents.size})",
                isSelected = uiState.statusFilter == EventStatus.REJECTED,
                color = Color(0xFFF44336),
                onClick = { viewModel.onStatusFilterChange(EventStatus.REJECTED) }
            )
        }

        SortDistanceRow(
            selectedSort = uiState.sortBy,
            selectedDistance = uiState.distanceKm,
            onSortChange = viewModel::onSortChange,
            onDistanceChange = viewModel::onDistanceChange,
            onMapClick = { showMapSheet = true }
        )

        Spacer(modifier = Modifier.size(7.dp))

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
                        contentPadding = PaddingValues(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                    ) {
                        Log.i("Moderator events" , "Los eventos del uiState : ${uiState.filteredEvents}")
                        items(
                            items = uiState.filteredEvents,
                            key = { it.id },
                        ) { event ->
                            ModeratorEventCard(
                                navController = navController,
                                event = event,
                                onCardClick = { onEventClick(event.id) },
                                onAccept = { viewModel.onEventAccept(event) },
                                onReject = {onEventClick(event.id)},
                            )
                        }
                    }
                }
            }
        }
    }

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
private fun StatusChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color,
            selectedLabelColor = Color.White,
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
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
            text = stringResource(R.string.moderator_no_events),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
        )
    }
}