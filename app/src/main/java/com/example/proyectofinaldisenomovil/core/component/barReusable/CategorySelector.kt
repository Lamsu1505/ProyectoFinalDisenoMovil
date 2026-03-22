package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme
import com.example.proyectofinaldisenomovil.data.model.Event.EventCategory

// Colors matching the design
private val SelectedGreen = Color(0xFF4A8C5C)
private val UnselectedGray = Color(0xFFE0E0E0)
private val UnselectedTextGray = Color(0xFF6B6B6B)
private val PlusButtonGray = Color(0xFFD6D6D6)



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryEventsSelectorBar(
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<EventCategory?>(null) }

    val allCategories = EventCategory.values().toList()
    val visibleCategories = allCategories.take(3)
    val extraCategories = if (allCategories.size > 3) allCategories.drop(3) else emptyList()

    Column(modifier = Modifier.fillMaxWidth()) {

        // ── Main row: first 3 chips + "+" button ──
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
                    isSelected = selected == category,
                    onClick = {
                        selected = if (selected == category) null else category
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // "+" / "x" toggle button
            if (extraCategories.isNotEmpty()) {
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(42.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PlusButtonGray,
                        contentColor = UnselectedTextGray
                    )
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = if (expanded) "Cerrar categorías" else "Más categorías",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ── Expandable section: remaining categories ──
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
                        isSelected = selected == category,
                        onClick = {
                            selected = if (selected == category) null else category
                        }
                    )
                }
            }
        }

        // Bottom divider
        Divider(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = Color(0xFFD0D0D0),
            thickness = 1.dp
        )
    }
}

/**
 * A single rounded-pill category chip that toggles between
 * a filled green state (selected) and a light-gray outlined state (unselected).
 */
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
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) SelectedGreen else UnselectedGray,
            contentColor = if (isSelected) Color.White else UnselectedTextGray
        ),
        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFBDBDBD)) else null,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 2.dp else 0.dp
        )
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Composable
fun CategoryBarNotifications(
    onCategorySelected: (String) -> Unit = {}
) {
    val categories = listOf("Todas", "No leidas", "Eventos", "Coments")
    var selectedCategory by remember { mutableStateOf("Todas") }

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
            categories.forEach { category ->
                NotificationTab(
                    label = category,
                    isSelected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                        onCategorySelected(category)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * A single notification tab — green pill when selected, transparent when not.
 */
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


@Preview(showBackground = true)
@Composable
fun PreviewCategorySelect() {
    ProyectoFinalDisenoMovilTheme {
        CategoryEventsSelectorBar()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryNotification() {
    ProyectoFinalDisenoMovilTheme {
        CategoryBarNotifications(
        )
    }
}