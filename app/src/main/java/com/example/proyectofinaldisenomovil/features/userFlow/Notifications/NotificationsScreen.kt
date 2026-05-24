package com.example.proyectofinaldisenomovil.features.userFlow.Notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.component.barReusable.CategoryBarNotifications
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.domain.model.AppNotification
import com.example.proyectofinaldisenomovil.domain.model.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    notificationsViewModel: NotificationsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by notificationsViewModel.uiState.collectAsState()
    val groupedNotifications by notificationsViewModel.groupedNotifications.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        text = stringResource(R.string.notifications_title),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.notifications_back),
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            CategoryBarNotifications(
                onCategorySelected = { filter ->
                    notificationsViewModel.onFilterSelected(filter)
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.notifications_mark_all_read),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        notificationsViewModel.markAllAsRead()
                    }
                )
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (groupedNotifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.notifications_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    groupedNotifications.forEach { (sectionTitle, notifications) ->
                        item(key = "header_$sectionTitle") {
                            Text(
                                text = sectionTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                modifier = Modifier.padding(
                                    start = 16.dp, end = 16.dp,
                                    top = 20.dp, bottom = 8.dp
                                )
                            )
                        }

                        items(
                            items = notifications,
                            key = { it.id }
                        ) { notification ->
                            NotificationItemRow(
                                notification = notification,
                                viewModel = notificationsViewModel
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItemRow(
    notification: AppNotification,
    viewModel: NotificationsViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(if (notification.read) Color.Transparent else MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
            .clickable { viewModel.markAsRead(notification.id) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        NotificationIcon(notification)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = notification.body,
                fontSize = 13.sp,
                color = if (notification.read) Color.Gray else MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = viewModel.getTimeAgo(notification.createdAt),
                fontSize = 11.sp,
                color = Color.Gray
            )
        }

        if (!notification.read) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
private fun NotificationIcon(notification: AppNotification) {
    val iconSize = 40.dp
    val containerSize = 48.dp
    
    Box(
        modifier = Modifier
            .size(containerSize)
            .clip(CircleShape)
            .background(
                when (notification.type) {
                    NotificationType.VERIFIED -> Color(0xFFE8F5E9)
                    NotificationType.REJECTED -> Color(0xFFFFEBEE)
                    NotificationType.LIKE -> Color(0xFFFCE4EC)
                    NotificationType.COMMENT -> Color(0xFFE3F2FD)
                    NotificationType.SAVE -> Color(0xFFFFF3E0)
                    NotificationType.EDITED -> Color(0xFFF3E5F5)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when (notification.type) {
                NotificationType.COMMENT -> Icons.AutoMirrored.Filled.Comment
                NotificationType.REJECTED -> Icons.Default.Clear
                NotificationType.VERIFIED -> Icons.Default.CheckCircle
                NotificationType.NEW_EVENT, NotificationType.NEW_EVENT_NEARBY -> Icons.Default.Event
                NotificationType.FINALIZED -> Icons.Default.CheckCircle
                NotificationType.LIKE -> Icons.Default.Favorite
                NotificationType.SAVE -> Icons.Default.Bookmark
                NotificationType.EDITED -> Icons.Default.Edit
            },
            contentDescription = null,
            tint = when (notification.type) {
                NotificationType.VERIFIED -> Color(0xFF4CAF50)
                NotificationType.REJECTED -> Color(0xFFF44336)
                NotificationType.LIKE -> Color(0xFFE91E63)
                NotificationType.COMMENT -> Color(0xFF2196F3)
                NotificationType.SAVE -> Color(0xFFFF9800)
                NotificationType.EDITED -> Color(0xFF9C27B0)
                else -> MaterialTheme.colorScheme.primary
            },
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationsScreen() {
    ProyectoFinalDisenoMovilTheme {
        NotificationsScreen()
    }
}
