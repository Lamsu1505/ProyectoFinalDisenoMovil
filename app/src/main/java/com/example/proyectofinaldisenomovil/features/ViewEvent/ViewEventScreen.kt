package com.example.proyectofinaldisenomovil.features.ViewEvent

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppTopBar
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import kotlinx.coroutines.NonCancellable.key
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEventScreen(
    navController: NavController,
    viewEventViewModel: ViewEventViewModel = viewModel()
) {
    val state = viewEventViewModel.uiState
    val numberFormat = NumberFormat.getNumberInstance(Locale("es", "CO"))

    Scaffold(
        topBar = {
            AppTopBar(
                navController ,
                title = "Evento"
            )
        },
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Seccion de imagenes ──
            item {
                EventImageSection(state)
            }

            // ── Info Card ──
            item {
                EventInfoCard(
                    state = state,
                    isInterested = viewEventViewModel.isInterested,
                    isConfirmed = viewEventViewModel.isConfirmed,
                    onInterestedClick = { viewEventViewModel.toggleInterested() },
                    onConfirmedClick = { viewEventViewModel.toggleConfirmed() },
                    numberFormat = numberFormat
                )
            }

            // ── Description Section ──
            item {
                EventDescriptionSection(state.description)
            }

            // ── Divider ──
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            // ── Location Section ──
            item {
                EventLocationSection(state.imageRes)
            }

            // ── Divider before comments ──
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }


            item {
                Text(
                    text = "Comentarios",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(
                        start = 0.dp, end = 20.dp,
                        top = 16.dp, bottom = 10.dp
                    )
                )
            }

            // ── Comment items ──
            items(
                items = state.comments,
                key = { it.id }
            ) { comment ->
                CommentItem(comment = comment)
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


@Composable
private fun EventImageSection(state: ViewEventUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = state.imageRes),
            contentDescription = "Imagen del evento",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Categoria
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 18.dp, vertical = 2.dp)
                .align(Alignment.TopStart)
                .width(90.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.category,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }

        // Page indicator
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.70f),
                    shape = RoundedCornerShape(7.dp)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${state.currentImageIndex} / ${state.totalImages}",
                color = MaterialTheme.colorScheme.background,
                fontSize = 13.sp
            )
        }
    }
}


@Composable
private fun EventInfoCard(
    state: ViewEventUiState,
    isInterested: Boolean,
    isConfirmed: Boolean,
    onInterestedClick: () -> Unit,
    onConfirmedClick: () -> Unit,
    numberFormat: NumberFormat
) {
    Column(
        modifier = Modifier
            .width(400.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape( bottomEnd = 20.dp , bottomStart = 20.dp )
            )
            .padding(horizontal = 10.dp, vertical = 18.dp)

    ) {

        // Title
        Text(
            text = state.title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 26.sp,
            maxLines = 2,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant)
        Spacer(modifier = Modifier.height(14.dp))

        // Two-column layout: left info rows, right creator + buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left column – info rows
            Column(
                modifier = Modifier.weight(4f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoRow(
                    icon = Icons.Default.DateRange,
                    text = state.date
                )
                InfoRow(
                    icon = Icons.Default.Timer,
                    text = state.time
                )
                InfoRow(
                    icon = Icons.Default.LocationOn,
                    text = "${state.location}"
                )
                InfoRow(
                    icon = if (isInterested) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    text = "${numberFormat.format(state.likes)} likes",
                    iconTint = if (isInterested) Color.Red else Color.Gray
                )
                InfoRow(
                    icon = Icons.Default.Groups,
                    text = "${numberFormat.format(state.currentAttendees)} / ${
                        numberFormat.format(
                            state.maxAttendees
                        )
                    }"
                )
            }

            // Right column – creator + action buttons
            Column(
                modifier = Modifier.weight(3f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Creator info
                Column(horizontalAlignment = Alignment.Start)
                {
                    Text(
                        text = "Creado por:",
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Text(
                        text = state.creatorName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // "Estoy interesado" button
                Button(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    onClick = onInterestedClick,
                    contentPadding = PaddingValues(8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isInterested)
                            MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.surface,
                        contentColor = if (isInterested)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    ),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isInterested) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isInterested) Color.Red else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp).weight(1f)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .weight(5f),
                            textAlign = TextAlign.Center,
                            text = "Estoy interesado",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }

                // "Confirmar asistencia" button
                Button(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    onClick = onInterestedClick,
                    contentPadding = PaddingValues(8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isInterested)
                            MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.surface,
                        contentColor = if (isInterested)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = if (isConfirmed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .weight(5f),
                        textAlign = TextAlign.Center,
                        text = "Confirmar asistencia",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    iconTint: Color = Color.Gray
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
        )
    }
}

@Composable
private fun EventDescriptionSection(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Descripción del evento",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = description,
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 22.sp
        )
    }
}


@Composable
private fun EventLocationSection(imageRes: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Ubicación exacta",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Placeholder map box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Ubicación en mapa",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Composable
private fun CommentItem(comment: CommentUiModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverflowing by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar circle with initials
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = comment.initials,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Name, time, and text
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = comment.authorName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = comment.timeAgo,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = comment.text,
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { result ->
                    if (!isExpanded) {
                        isOverflowing = result.hasVisualOverflow
                    }
                }
            )

            if (isOverflowing && !isExpanded) {
                Text(
                    text = "Mostrar más",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { isExpanded = true }
                        .padding(top = 2.dp)
                )
            }

            if (isExpanded) {
                Text(
                    text = "Mostrar menos",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { isExpanded = false }
                        .padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp) )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewEventScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        ViewEventScreen(navController = rememberNavController())
    }
}
