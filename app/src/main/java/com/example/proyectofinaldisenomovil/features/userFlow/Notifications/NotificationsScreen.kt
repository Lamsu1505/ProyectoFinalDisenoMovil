package com.example.proyectofinaldisenomovil.features.userFlow.Notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinaldisenomovil.core.component.barReusable.CategoryBarNotifications
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.domain.model.AppNotification
import com.example.proyectofinaldisenomovil.domain.model.NotificationType
import com.example.proyectofinaldisenomovil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    notificationsViewModel: NotificationsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by notificationsViewModel.uiState.collectAsState()
    val groupedNotifications = notificationsViewModel.getGroupedNotifications()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
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

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                CategoryBarNotifications(
                    onCategorySelected = { category ->
                        notificationsViewModel.onFilterSelected(category)
                    }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.notifications_mark_all_read),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            notificationsViewModel.markAllAsRead()
                        }
                    )
                }
            }

            groupedNotifications.forEach { (sectionTitle, notifications) ->
                item(key = "header_$sectionTitle") {
                    Text(
                        text = sectionTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(
                            start = 16.dp, end = 16.dp,
                            top = 20.dp, bottom = 1.dp
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

@Composable
private fun NotificationItemRow(
    notification: AppNotification,
    viewModel: NotificationsViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { viewModel.markAsRead(notification.id) },
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
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(80.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = viewModel.getTimeAgo(notification.createdAt),
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1976D2))
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationIcon(notification: AppNotification) {
    val iconSize = 48.dp
    when (notification.type) {
        NotificationType.COMMENT -> {
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.notification_comment),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        NotificationType.REJECTED -> {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.notification_event_rejected),
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(iconSize)
            )
        }

        NotificationType.VERIFIED -> {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.notification_event_verified),
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(iconSize)
            )
        }

        NotificationType.NEW_EVENT, NotificationType.NEW_EVENT_NEARBY -> {
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = stringResource(R.string.notification_new_event),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        NotificationType.FINALIZED -> {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.notification_event_finalized),
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationsScreen() {
    ProyectoFinalDisenoMovilTheme {
        NotificationsScreen()
    }
}
