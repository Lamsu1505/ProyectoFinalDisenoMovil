package com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.CategoryEventsSelectorBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.SearchTopBarApp
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.features.userFlow.home.DistanceComboBox
import com.example.proyectofinaldisenomovil.features.userFlow.home.OrderByComboBox
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LikedEventsScreen(
    viewModel: FavoritesViewModel = viewModel(),
    paddingValues: PaddingValues,
    onNotificationClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SearchTopBarApp(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                onNotificationsClick = onNotificationClick
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedRoute = ""
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category Selector
            item {
                CategoryEventsSelectorBar(
                    onCategorySelected = {}
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Filter Dropdowns
            item {
                FavoritesFilters()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Event List
            items(uiState.favoriteEvents) { event ->
                FavoriteEventCard(
                    event = event,
                    onToggleFavorite = { viewModel.onToggleFavorite(event.id) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun FavoritesFilters() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
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
                Text(
                    text = stringResource(R.string.filter_sort_by),
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,

                    )
                OrderByComboBox(listOf("Nombre", "Fecha", "Popularidad"),
                    selected = "Nombre",
                    onSelected = {}
                )
            }

            Row(
                modifier = Modifier.weight(3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = stringResource(R.string.filter_distance),
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                DistanceComboBox()
            }
        }
    }
}

@Composable
fun FavoriteEventCard(
    event: FavoriteEvent,
    onToggleFavorite: () -> Unit
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale("es", "CO"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = event.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )

                // Categoria tag
                Surface(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(bottomEnd = 16.dp),
                    modifier = Modifier.padding(top = 0.dp, start = 0.dp)
                ) {
                    Text(
                        text = event.category,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    // Columna izquierda Info
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconInfoRow(Icons.Default.DateRange, event.date)
                        IconInfoRow(Icons.Default.DateRange, event.time)
                        IconInfoRow(Icons.Default.LocationOn, "${event.location} (${event.distance})")
                    }

                    // Columna derecha Asistentes y Corazón
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(numberFormat.format(event.attendees), fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        }

                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = stringResource(R.string.liked_events_title),
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 13.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        LikedEventsScreen(paddingValues = PaddingValues())
    }
}
