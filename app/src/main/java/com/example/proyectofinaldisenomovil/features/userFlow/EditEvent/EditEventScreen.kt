package com.example.proyectofinaldisenomovil.features.userFlow.EditEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.theme.green
import com.example.proyectofinaldisenomovil.core.theme.orange
import com.example.proyectofinaldisenomovil.core.theme.whiteBackground
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent.ViewEventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    eventId: String,
    onBackClick: () -> Unit,
    viewModel: ViewEventViewModel = hiltViewModel() // Reutilizamos lógica de carga por ahora
) {
    val event by viewModel.currentEvent.collectAsState()
    val detailResult by viewModel.detailResult.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.findEventById(eventId)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Editar Evento",
                onBackClick = onBackClick
            )
        },
        bottomBar = { AppBottomBar(selectedRoute = "") },
        containerColor = whiteBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (detailResult) {
                is RequestResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = green)
                }
                is RequestResult.Success -> {
                    event?.let { safeEvent ->
                        EditEventContent(event = safeEvent)
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun EditEventContent(event: Event) {
    var title by remember { mutableStateOf(event.title) }
    var description by remember { mutableStateOf(event.description) }
    var address by remember { mutableStateOf(event.address) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            // Header con imagen actual
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                AsyncImage(
                    model = event.imageUrls.firstOrNull(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                            Text("Cambiar portada", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            EditField(label = "Título del evento", value = title, onValueChange = { title = it }, icon = Icons.Default.Title)
        }

        item {
            EditField(label = "Ubicación", value = address, onValueChange = { address = it }, icon = Icons.Default.LocationOn)
        }

        item {
            Text(
                text = "Descripción",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = green,
                modifier = Modifier.padding(start = 8.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = green,
                    unfocusedBorderColor = Color.Gray
                )
            )
        }

        item {
            Button(
                onClick = { /* TODO: Guardar cambios */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = orange),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Guardar cambios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun EditField(label: String, value: String, onValueChange: (String) -> Unit, icon: ImageVector) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = green,
            modifier = Modifier.padding(start = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(icon, contentDescription = null, tint = green) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = green,
                unfocusedBorderColor = Color.Gray
            )
        )
    }
}
