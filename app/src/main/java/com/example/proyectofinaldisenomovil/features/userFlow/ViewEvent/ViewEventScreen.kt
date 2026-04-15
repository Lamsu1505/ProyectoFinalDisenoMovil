package com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ViewEventScreen(
    eventId: String,
    onBackClick: () -> Unit,
    viewEventViewModel: ViewEventViewModel = hiltViewModel()
) {
    Log.e("ID", eventId)

    // 1. Observación de estados de forma reactiva
    val event by viewEventViewModel.currentEvent.collectAsState()
    val detailResult by viewEventViewModel.detailResult.collectAsState()
    val isInterested by viewEventViewModel.isInterested.collectAsState()
    val isConfirmed by viewEventViewModel.isConfirmed.collectAsState()

    val listState = rememberLazyListState()

    // 2. Disparador de carga inicial
    LaunchedEffect(eventId) {
        viewEventViewModel.findEventById(eventId)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = event?.title ?: stringResource(R.string.view_event_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = { AppBottomBar(selectedRoute = "") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 3. Gestión de estados de la petición (Loading, Success, Error)
            when (detailResult) {
                is RequestResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is RequestResult.Failure -> {
                    ErrorState((detailResult as RequestResult.Failure).errorMessage)
                }
                is RequestResult.Success -> {
                    event?.let { safeEvent ->
                        EventDetailContent(
                            event = safeEvent,
                            isInterested = isInterested,
                            isConfirmed = isConfirmed,
                            onInterestedClick = viewEventViewModel::toggleInterested,
                            onConfirmedClick = viewEventViewModel::toggleConfirmed,
                            listState = listState
                        )
                    }
                }
                else -> { /* Estado inicial */ }
            }
        }
    }
}

@Composable
private fun EventDetailContent(
    event: Event,
    isInterested: Boolean,
    isConfirmed: Boolean,
    onInterestedClick: () -> Unit,
    onConfirmedClick: () -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        item { EventImageHeader(event) }
        item {
            EventInfoSection(
                event = event,
                isInterested = isInterested,
                isConfirmed = isConfirmed,
                onInterestedClick = onInterestedClick,
                onConfirmedClick = onConfirmedClick
            )
        }
        item { EventDescription(event.description) }
        item { EventMapPlaceholder() }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
private fun EventImageHeader(event: Event) {
    Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
        AsyncImage(
            model = event.imageUrls.firstOrNull(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Badge de Categoría
        Surface(
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart),
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = event.category.label,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun EventInfoSection(
    event: Event,
    isInterested: Boolean,
    isConfirmed: Boolean,
    onInterestedClick: () -> Unit,
    onConfirmedClick: () -> Unit
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale("es", "CO"))
    val dateFormat = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "CO"))
    val timeFormat = SimpleDateFormat("h:mm a", Locale("es", "CO"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp)
    ) {
        Text(
            text = event.title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // Columna izquierda: Datos
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                IconLabel(Icons.Default.CalendarToday, event.startDate?.let { dateFormat.format(it.toDate()) } ?: "")
                IconLabel(Icons.Default.Schedule, event.startDate?.let { timeFormat.format(it.toDate()) } ?: "")
                IconLabel(Icons.Default.LocationOn, event.address)
                IconLabel(
                    icon = if (isInterested) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    label = "${event.importantVotes} interesados",
                    tint = if (isInterested) Color.Red else Color.White
                )
            }

            // Columna derecha: Acciones
            Column(modifier = Modifier.width(140.dp), horizontalAlignment = Alignment.End) {
                Text("Organizado por:", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                Text(event.authorName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)

                Spacer(modifier = Modifier.height(16.dp))

                ActionButton(
                    text = if (isInterested) "Interesado" else "Me interesa",
                    icon = if (isInterested) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    isSelected = isInterested,
                    onClick = onInterestedClick
                )

                Spacer(modifier = Modifier.height(8.dp))

                ActionButton(
                    text = if (isConfirmed) "Confirmado" else "Confirmar",
                    icon = Icons.Default.Check,
                    isSelected = isConfirmed,
                    isPrimary = true,
                    onClick = onConfirmedClick
                )
            }
        }
    }
}

@Composable
private fun IconLabel(icon: ImageVector, label: String, tint: Color = Color.White) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.White, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(38.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected || isPrimary) Color.White else Color.Transparent,
            contentColor = if (isSelected || isPrimary) MaterialTheme.colorScheme.primary else Color.White
        ),
        border = if (!isSelected && !isPrimary) BorderStroke(1.dp, Color.White) else null,
        contentPadding = PaddingValues(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(icon, null, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EventDescription(description: String) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Descripción", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
    }
}

@Composable
private fun EventMapPlaceholder() {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text("Ubicación", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp)).background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Mapa próximamente", color = Color.DarkGray)
        }
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
    }
}


@Preview(showBackground = true)
@Composable
fun ViewEventScreenPreview(){
    ViewEventScreen(eventId = "evt_001", onBackClick = {})
}