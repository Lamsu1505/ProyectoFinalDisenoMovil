package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.features.userFlow.Notifications.NotificationFilter
import com.example.proyectofinaldisenomovil.core.theme.green

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryEventsSelectorBar(
    selectedCategory: EventCategory? = null,
    onCategorySelected: (EventCategory?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val allCategories = EventCategory.entries.toList()
    val visibleCategories = allCategories.take(3)
    val extraCategories = if (allCategories.size > 3) allCategories.drop(3) else emptyList()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            visibleCategories.forEach { category ->
                CategoryChip(
                    label = category.label,
                    isSelected = selectedCategory == category,
                    onClick = {
                        val newSelection = if (selectedCategory == category) null else category
                        onCategorySelected(newSelection)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            if (extraCategories.isNotEmpty()) {
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(42.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFD6D6D6),
                        contentColor = Color(0xFF6B6B6B)
                    )
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                extraCategories.forEach { category ->
                    CategoryChip(
                        label = category.label,
                        isSelected = selectedCategory == category,
                        onClick = {
                            val newSelection = if (selectedCategory == category) null else category
                            onCategorySelected(newSelection)
                        }
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = Color(0xFFD0D0D0),
            thickness = 1.dp
        )
    }
}

@Composable
private fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) green else Color(0xFFE0E0E0),
            contentColor = if (isSelected) Color.White else Color(0xFF6B6B6B)
        ),
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFBDBDBD)) else null,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Composable
fun CategoryBarNotifications(
    onCategorySelected: (NotificationFilter) -> Unit = {}
) {
    val items = listOf(
        stringResource(R.string.notifications_filter_all) to NotificationFilter.ALL,
        stringResource(R.string.notifications_filter_unread) to NotificationFilter.UNREAD,
        stringResource(R.string.notifications_filter_events) to NotificationFilter.EVENTS,
        stringResource(R.string.notifications_filter_comments) to NotificationFilter.COMMENTS
    )
    
    var selectedFilter by remember { mutableStateOf(NotificationFilter.ALL) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (label, filter) ->
                NotificationTab(
                    label = label,
                    isSelected = selectedFilter == filter,
                    onClick = {
                        selectedFilter = filter
                        onCategorySelected(filter)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NotificationTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            fontSize = if (isSelected) 14.sp else 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
