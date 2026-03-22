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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinaldisenomovil.core.component.barReusable.CategoryBarNotifications
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    notificationsViewModel: NotificationsViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val groupedNotifications = notificationsViewModel.getGroupedNotifications()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // el padding ya incluye top, bottom (BottomBar) y demás insets
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text(
                    text = "Notificaciones",
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            actions = {
                Spacer(modifier = Modifier.width(48.dp))
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                CategoryBarNotifications(
                    onCategorySelected = { category ->
                        notificationsViewModel.selectCategory(category)
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
                        text = "Marcar todo como leído",
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
                    NotificationItemRow(notification = notification)
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
private fun NotificationItemRow(notification: NotificationItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
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
                text = notification.subtitle,
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
                text = notification.timeAgo,
                textAlign = TextAlign.End,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                if (notification.isUnread) {
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
private fun NotificationIcon(notification: NotificationItem) {
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
                Text(
                    text = notification.initials,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        NotificationType.EVENT_REJECTED -> {
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = "Evento rechazado",
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(iconSize)
            )
        }

        NotificationType.EVENT_VERIFIED -> {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Evento verificado",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(iconSize)
            )
        }

        NotificationType.NEW_EVENT_NEARBY -> {
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {}
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