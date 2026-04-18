package com.example.proyectofinaldisenomovil.features.userFlow.MyEvents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.SearchTopBarApp
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.core.theme.green
import com.example.proyectofinaldisenomovil.core.theme.orange
import com.example.proyectofinaldisenomovil.core.theme.red
import com.example.proyectofinaldisenomovil.core.theme.whiteBackground
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MyEventsScreen(
    viewModel: MyEventsViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    onNotificationClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onEventClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var showRejectionDialog by remember { mutableStateOf(false) }
    var selectedRejectionReason by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
                SearchTopBarApp(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange,
                    onNotificationsClick = onNotificationClick,
                )
        },
        bottomBar = {
            AppBottomBar(selectedRoute = "")
        },
        containerColor = whiteBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Status Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusTab(
                    label = "Activos",
                    isSelected = uiState.selectedStatus == EventStatus.VERIFIED,
                    onClick = { viewModel.onStatusChange(EventStatus.VERIFIED) }
                )
                StatusTab(
                    label = "Finalizados",
                    isSelected = uiState.selectedStatus == EventStatus.RESOLVED,
                    onClick = { viewModel.onStatusChange(EventStatus.RESOLVED) }
                )
                StatusTab(
                    label = "Pendientes",
                    isSelected = uiState.selectedStatus == EventStatus.PENDING_REVIEW,
                    onClick = { viewModel.onStatusChange(EventStatus.PENDING_REVIEW) }
                )
                StatusTab(
                    label = "Descartados",
                    isSelected = uiState.selectedStatus == EventStatus.REJECTED,
                    onClick = { viewModel.onStatusChange(EventStatus.REJECTED) }
                )
            }

            // Summary info
            Text(
                text = "${uiState.filteredEvents.size} Eventos ${getStatusTextLabel(uiState.selectedStatus)}",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = green)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.filteredEvents) { event ->
                        MyEventCard(
                            event = event,
                            onEditClick = { onEditClick(event.id) },
                            onEventClick = { onEventClick(event.id) },
                            onShowRejectionReason = { reason ->
                                selectedRejectionReason = reason
                                showRejectionDialog = true
                            }
                        )
                    }
                }
            }
        }
        if (showRejectionDialog) {
            AlertDialog(
                onDismissRequest = { showRejectionDialog = false },
                title = {
                    Text(
                        "Motivo del descarte",
                        fontWeight = FontWeight.Bold,
                        color = red
                    )
                },
                text = {
                    Text(
                        selectedRejectionReason.ifBlank { "No se proporcionó un motivo detallado." },
                        fontSize = 16.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showRejectionDialog = false }) {
                        Text("Entendido", color = green, fontWeight = FontWeight.Bold)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White
            )
        }
    }
}

@Composable
fun StatusTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = if (isSelected) green else Color(0xFFE0E0E0),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MyEventCard(
    event: Event,
    onEditClick: () -> Unit,
    onEventClick: () -> Unit,
    onShowRejectionReason: (String) -> Unit
) {
    val dateFormat = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "CO"))
    val timeFormat = SimpleDateFormat("h:mm a", Locale("es", "CO"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEventClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Image section
                Box(modifier = Modifier.size(width = 140.dp, height = 90.dp)) {
                    AsyncImage(
                        model = event.imageUrls.firstOrNull(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        color = orange,
                        shape = RoundedCornerShape(bottomEnd = 12.dp, topStart = 12.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = event.category.label,
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info section
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    InfoRow(Icons.Default.CalendarToday, event.startDate?.let { dateFormat.format(it.toDate()) } ?: "Jueves 28 de febrero")
                    InfoRow(Icons.Default.Schedule, event.startDate?.let { timeFormat.format(it.toDate()) } ?: "6:00 pm")
                    InfoRow(Icons.Default.LocationOn, event.address.ifBlank { "Estadio centenario" })
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.title.ifBlank { "Partido de la Historia (Real Madrid vs Deportes ..." },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // FILA INFERIOR DINÁMICA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                when (event.status) {
                    EventStatus.PENDING_REVIEW -> {
                        // Diseño para PENDIENTES
                        Icon(
                            imageVector = Icons.Default.PendingActions,
                            contentDescription = null,
                            tint = orange,
                            modifier = Modifier.size(36.dp).padding(start = 8.dp)
                        )

                        Button(
                            onClick = onEventClick,
                            colors = ButtonDefaults.buttonColors(containerColor = orange),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Ver evento", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    EventStatus.REJECTED -> {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = red,
                            modifier = Modifier.size(36.dp).padding(start = 8.dp)
                        )

                        Button(
                            onClick = { onShowRejectionReason(event.rejectionReason ?: "") },
                            colors = ButtonDefaults.buttonColors(containerColor = orange),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.Help, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Razon de descarte", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    else -> {
                        // Diseño para ACTIVOS / FINALIZADOS
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(30.dp))
                            Text(text = "${event.importantVotes} likes", modifier = Modifier.padding(start = 4.dp), fontSize = 12.sp, color = Color.Gray)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Group, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Text(text = "${event.currentAttendees} / ${event.maxAttendees ?: "∞"}", modifier = Modifier.padding(start = 4.dp), fontSize = 12.sp, color = Color.Gray)
                        }

                        Button(
                            onClick = onEditClick,
                            colors = ButtonDefaults.buttonColors(containerColor = orange),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Editar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 12.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

private fun getStatusTextLabel(status: EventStatus): String {
    return when(status) {
        EventStatus.VERIFIED -> "están activos"
        EventStatus.RESOLVED -> "fueron finalizados"
        EventStatus.PENDING_REVIEW -> "están pendientes"
        EventStatus.REJECTED -> "fueron Descartados"
    }
}
