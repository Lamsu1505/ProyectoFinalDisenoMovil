package com.example.proyectofinaldisenomovil.core.component.moderator

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinaldisenomovil.domain.model.Event.EventCategory
import com.example.proyectofinaldisenomovil.core.theme.*

/**
 * Horizontal scrollable row of category filter chips.
 *
 * Renders one chip per [EventCategory] plus a trailing "+" chip for future
 * extension. The currently active [selectedCategory] is filled green; others
 * are outlined with a neutral border.
 *
 * @param selectedCategory  The currently active filter, or null for "all".
 * @param onCategorySelect  Called with the tapped category (null when "+" tapped).
 */
@Composable
fun CategoryFilterChips(
    selectedCategory: EventCategory?,
    onCategorySelect: (EventCategory?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // One chip per category
        EventCategory.entries.forEach { category ->
            val isSelected = selectedCategory == category
            FilterChip(
                selected = isSelected,
                onClick  = {
                    // Tapping the already-selected chip deselects (shows all)
                    onCategorySelect(if (isSelected) null else category)
                },
                label = {
                    Text(
                        text     = category.label,
                        fontSize = 13.sp,
                    )
                },
                shape  = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor     = MaterialTheme.colorScheme.primary,
                    selectedLabelColor         = Color.White,
                    containerColor             = Color.Transparent,
                    labelColor                 = MaterialTheme.colorScheme.onSurface,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled            = true,
                    selected           = isSelected,
                    borderColor        = MaterialTheme.colorScheme.onBackground,
                    selectedBorderColor = Color.Transparent,
                    borderWidth        = 1.dp,
                ),
            )
        }

        // "+" chip for adding more filters in the future
        IconButton(
            onClick  = { /* TODO: open extended filter bottom sheet only if we want */ },
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                imageVector        = Icons.Default.Add,
                contentDescription = "Mas filtros",
                tint               = MaterialTheme.colorScheme.onSurface,
                modifier           = Modifier.size(18.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryFilterChipsPreview() {
    ProyectoFinalDisenoMovilTheme() {
        CategoryFilterChips(
            selectedCategory = null,
            onCategorySelect = {},
        )
    }
}