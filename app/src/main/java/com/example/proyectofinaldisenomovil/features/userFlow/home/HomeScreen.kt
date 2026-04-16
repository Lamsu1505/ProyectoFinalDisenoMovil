package com.example.proyectofinaldisenomovil.features.userFlow.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.proyectofinaldisenomovil.core.component.barReusable.CategoryEventsSelectorBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.SearchTopBarApp
import com.example.proyectofinaldisenomovil.domain.model.Event.Event
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    onNotificationClick: () -> Unit,
    onEventClick : (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    // Observa el estado del ViewModel
    val events by homeViewModel.events.collectAsState(initial = emptyList())
    val orderBy by homeViewModel.orderBy.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
    ) {
        // TopBar suelta, sin Scaffold
        SearchTopBarApp(
            query = query,
            onQueryChange = {
                query = it
                homeViewModel.onSearchQueryChanged(it)
            },
            onNotificationsClick = onNotificationClick
        )

        // Barra de categorías
        CategoryEventsSelectorBar(
            onCategorySelected = { homeViewModel.onCategorySelected(it) }
        )

        Spacer(modifier = Modifier.size(7.dp))

        // Filtros de orden y distancia
        FiltersBar(
            selectedOrder = orderBy,
            onOrderSelected = { homeViewModel.onOrderByChanged(it) }
        )

        Spacer(modifier = Modifier.size(7.dp))

        if (events.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.home_no_events),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(
                    items = events,
                    key = { it.id }
                ) { event ->
                    EventCard(
                        event = event,
                        onEventClick = onEventClick,
                        onLikeClick = {}
                    )
                }
            }
        }
    }
}


@Composable
fun FiltersBar(
    selectedOrder: String,
    onOrderSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier.weight(4f),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.filter_sort_by), fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                OrderByComboBox(
                    options = listOf(
                        stringResource(R.string.filter_name),
                        stringResource(R.string.filter_date),
                        stringResource(R.string.filter_votes)
                    ),
                    selected = selectedOrder,
                    onSelected = onOrderSelected
                )
            }

            Row(
                modifier = Modifier.weight(3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(stringResource(R.string.filter_distance), fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                DistanceComboBox()
            }
        }
    }
}


@Composable
fun OrderByComboBox(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
    ) {
        Row(
            Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(2.dp)
                .padding(start = 8.dp)
                .width(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(3f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun DistanceComboBox() {
    val options = listOf("1Km", "5Km", "10Km", "30Km", "50km", "100Km", "+150Km")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[3]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(2.dp)
                .padding(start = 8.dp)
                .width(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedOption,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.width(120.dp),
                        text = {
                            Text(
                                text = option,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun EventCard(
    event: Event,
    onEventClick: (String) -> Unit,
    onLikeClick: () -> Unit = {}
) {

    val categoryColor = when (event.category) {
        EventCategory.DEPORTES -> Color(0xFFE65100)
        EventCategory.CULTURA -> Color(0xFF6A1B9A)
        EventCategory.VOLUNTARIADO -> Color(0xFF2E7D32)
        EventCategory.SOCIAL -> Color(0xFFC62828)
        else -> Color.Gray
    }

    val categoryLabel = when (event.category) {
        EventCategory.DEPORTES -> "Deportes"
        EventCategory.CULTURA -> "Cultura"
        EventCategory.VOLUNTARIADO -> "Voluntariado"
        EventCategory.SOCIAL -> "Social"
        else -> ""
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onEventClick(event.id) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column {

            // ─────────── Imagen + overlays ───────────
            Box {

                AsyncImage(
                    model = event.thumbnailUrl,
                    contentDescription = event.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )

                // Badge categoría
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(categoryColor, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = categoryLabel,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Botón "me interesa"
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            Color.Black.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Me interesa",
                        tint = Color.White
                    )
                }
            }

            // ─────────── Contenido ───────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                // Título
                Text(
                    text = event.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Meta info (fecha + asistentes)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        Modifier.weight(3f),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(26.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = event.startDate.toString(),
                            fontSize = 15.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }

                    Row(
                        Modifier.weight(1f)

                    ) {

                        // Fecha (solo si existe)
                        event.startDate?.let {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = event.startDate.toString(),
                                fontSize = 13.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                        }

                        // Asistentes
                        Icon(
                            Icons.Filled.People,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = if (event.maxAttendees != null)
                                "${event.currentAttendees}/${event.maxAttendees}"
                            else
                                "${event.currentAttendees}",
                            fontSize = 15.sp,
                            color = Color.Gray
                        )

                    }
                }

                // Ubicación (simplificada)
                if (event.address.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(26.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = event.address,
                            fontSize = 15.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        HomeScreen(
            paddingValues = PaddingValues(0.dp),
            onNotificationClick = {},
            onEventClick = {}
        )
    }
}