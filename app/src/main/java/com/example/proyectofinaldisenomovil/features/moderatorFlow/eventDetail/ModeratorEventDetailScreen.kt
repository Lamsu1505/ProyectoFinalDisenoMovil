package com.example.proyectofinaldisenomovil.features.moderatorFlow.eventDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.proyectofinaldisenomovil.core.component.moderator.ConfirmationDialog
import com.example.proyectofinaldisenomovil.core.component.moderator.LogoutDialog
import com.example.proyectofinaldisenomovil.core.component.moderator.state.Moderatoreventdetailuistate
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.R
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ModeratorEventDetailScreen(
    eventId: String,
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: ModeratorEventDetailViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    ModeratorEventDetailScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onLogout = onLogout,
        onLogoutClick = viewModel::onLogoutClick,
        onLogoutDismiss = viewModel::onLogoutDismiss,
        onImageIndexChange = viewModel::onImageIndexChange,
        onAcceptEvent = viewModel::onAcceptEvent,
        onRejectClick = viewModel::onRejectClick,
        onRejectionReasonChange = viewModel::onRejectionReasonChange,
        onRejectConfirm = viewModel::onRejectConfirm,
        onRejectDialogDismiss = viewModel::onRejectDialogDismiss,
        modifier = modifier
    )
}

@Composable
fun ModeratorEventDetailScreenContent(
    uiState: Moderatoreventdetailuistate,
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    onLogoutClick: () -> Unit,
    onLogoutDismiss: () -> Unit,
    onImageIndexChange: (Int) -> Unit,
    onAcceptEvent: () -> Unit,
    onRejectClick: () -> Unit,
    onRejectionReasonChange: (String) -> Unit,
    onRejectConfirm: () -> Unit,
    onRejectDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.moderator_back),
                        tint = Color.White,
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.moderator_detail_title),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                IconButton(onClick = onLogoutClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = stringResource(R.string.moderator_logout),
                        tint = Color.White,
                    )
                }
            }
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                uiState.event != null -> {
                    EventDetailContent(
                        event = uiState.event,
                        currentImageIndex = uiState.currentImageIndex,
                        onImageIndexChange = onImageIndexChange,
                        onAcceptClick = onAcceptEvent,
                        onRejectClick = onRejectClick,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                else -> {
                    Text(
                        text = stringResource(R.string.moderator_event_not_found),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    )
                }
            }
        }
    }

    // Dialogs
    if (uiState.showRejectDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.moderator_confirm_title),
            bodyText = stringResource(R.string.moderator_confirm_reject),
            showReasonField = true,
            reasonValue = uiState.rejectionReason,
            onReasonChange = onRejectionReasonChange,
            reasonError = uiState.rejectionReasonError,
            isLoading = uiState.isSubmittingVerification,
            onConfirm = onRejectConfirm,
            onDismiss = onRejectDialogDismiss,
        )
    }

    if (uiState.showLogoutDialog) {
        LogoutDialog(
            moderatorName = "Moderador",
            onConfirmLogout = {
                onLogoutDismiss()
                onLogout()
            },
            onDismiss = onLogoutDismiss,
        )
    }
}

@Composable
private fun EventDetailContent(
    event: com.example.proyectofinaldisenomovil.domain.model.Event.Event,
    currentImageIndex: Int,
    onImageIndexChange: (Int) -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val dateFormatter = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es"))
    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

    Column(
        modifier = modifier.verticalScroll(scrollState),
    ) {
        // Image Carousel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
        ) {
            AsyncImage(
                model = event.imageUrls.getOrNull(currentImageIndex) ?: "",
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            // Category Badge
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart),
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.secondary,
            ) {
                Text(
                    text = event.category.label,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }

            // Image Indicators
            if (event.imageUrls.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    event.imageUrls.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentImageIndex) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == currentImageIndex)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        Color.White.copy(alpha = 0.5f)
                                ),
                        )
                    }
                }
            }
        }

        // Event Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                // Title
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1.dp.value))
                Spacer(Modifier.height(16.dp))

                // Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = event.startDate?.let { dateFormatter.format(it.toDate()) } ?: stringResource(R.string.moderator_date_not_defined),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = event.startDate?.let { timeFormatter.format(it.toDate()) } ?: "--",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = event.address.ifBlank { stringResource(R.string.moderator_location_not_defined) },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1.dp.value))
                Spacer(Modifier.height(16.dp))

                // Description
                Text(
                    text = stringResource(R.string.moderator_description_label),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = event.description.ifBlank { stringResource(R.string.moderator_no_description) },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 22.sp,
                )
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onRejectClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = stringResource(R.string.moderator_reject),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Button(
                onClick = onAcceptClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text(
                    text = stringResource(R.string.moderator_accept),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModeratorEventDetailScreenPreview() {
    ProyectoFinalDisenoMovilTheme {
        ModeratorEventDetailScreenContent(
            uiState = Moderatoreventdetailuistate(
                isLoading = false,
                event = com.example.proyectofinaldisenomovil.domain.model.Event.Event(
                    id = "1",
                    title = "Evento de prueba",
                    description = "Esta es una descripción de prueba para el preview.",
                    address = "Calle Falsa 123",
                    imageUrls = listOf("https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?w=800"),
                    category = com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory.CULTURA
                )
            ),
            onBackClick = {},
            onLogout = {},
            onLogoutClick = {},
            onLogoutDismiss = {},
            onImageIndexChange = {},
            onAcceptEvent = {},
            onRejectClick = {},
            onRejectionReasonChange = {},
            onRejectConfirm = {},
            onRejectDialogDismiss = {}
        )
    }
}
