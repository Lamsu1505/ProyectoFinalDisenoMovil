package com.example.proyectofinaldisenomovil.core.component.moderator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventStatus
import com.example.proyectofinaldisenomovil.core.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ModeratorEventCard(
    navController: NavController,
    event: Event,
    onCardClick: (String) -> Unit,
    onAccept: (Event) -> Unit,
    onReject: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusColor = when (event.status) {
        EventStatus.PENDING_REVIEW -> Color(0xFFFFA000)
        EventStatus.VERIFIED -> Color(0xFF4CAF50)
        EventStatus.REJECTED -> Color(0xFFF44336)
        EventStatus.RESOLVED -> Color(0xFF2196F3)
    }
    val statusLabel = when (event.status) {
        EventStatus.PENDING_REVIEW -> "Pendiente"
        EventStatus.VERIFIED -> "Verificado"
        EventStatus.REJECTED -> "Rechazado"
        EventStatus.RESOLVED -> "Resuelto"
    }

    Card(
        modifier  = modifier
            .fillMaxWidth()
            .clickable { onCardClick(event.id) },
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            ) {
                AsyncImage(
                    model = event.imageUrls.firstOrNull(),
                    contentDescription = event.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondary,
                ) {
                    Text(
                        text = event.category.label,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    )
                }

                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopEnd),
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor,
                ) {
                    Text(
                        text = statusLabel,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        MetaRow(
                            icon = Icons.Outlined.DateRange,
                            label = event.startDate?.let {
                                SimpleDateFormat("EEEE d 'de' MMM", Locale("es")).format(it.toDate())
                            } ?: "Fecha no definida",
                        )
                        MetaRow(
                            icon = Icons.Outlined.DateRange,
                            label = event.startDate?.let {
                                SimpleDateFormat("h:mm a", Locale.getDefault()).format(it.toDate())
                            } ?: "--",
                        )
                        MetaRow(
                            icon = Icons.Outlined.LocationOn,
                            label = event.address.ifBlank { "Ubicacion no definida" },
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.End,
                    ) {
                        when (event.status) {
                            EventStatus.PENDING_REVIEW -> {
                                ActionButton(
                                    label = "Aceptar",
                                    color = Color(0xFF4CAF50),
                                    icon = Icons.Filled.CheckCircle,
                                    onClick = { onAccept(event) },
                                )
                                ActionButton(
                                    label = "Rechazar",
                                    color = Color(0xFFF44336),
                                    icon = Icons.Filled.Close,
                                    onClick = { onReject(event.id) },
                                )
                            }
                            EventStatus.VERIFIED -> {
                                ActionButton(
                                    label = "Rechazar",
                                    color = Color(0xFFF44336),
                                    icon = Icons.Filled.Close,
                                    onClick = { onReject(event.id) },
                                )
                            }
                            EventStatus.REJECTED -> {
                                ActionButton(
                                    label = "Verificar",
                                    color = Color(0xFF4CAF50),
                                    icon = Icons.Filled.CheckCircle,
                                    onClick = { onAccept(event) },
                                )
                            }
                            EventStatus.RESOLVED -> { }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetaRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ActionButton(
    label: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        modifier = Modifier.height(32.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = Color.White,
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
fun ModeratorEventCardPreview() {
    ProyectoFinalDisenoMovilTheme() {
        ModeratorEventCard(
            navController = rememberNavController(),
            event = Event(
                id = "1",
                title = "Concierto de Rock en el Parque",
                description = "Descripción del evento",
                category = com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory.DEPORTES,
                startDate = com.google.firebase.Timestamp.now(),
                endDate = com.google.firebase.Timestamp.now(),
                address = "Parque Central, Ciudad",
                imageUrls = listOf("https://example.com/event-image.jpg"),
                status = EventStatus.PENDING_REVIEW
            ),
            onCardClick = {},
            onAccept = {},
            onReject = {},
        )
    }
}