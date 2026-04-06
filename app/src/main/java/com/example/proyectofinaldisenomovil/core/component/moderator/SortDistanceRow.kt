package com.example.proyectofinaldisenomovil.core.component.moderator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinaldisenomovil.core.component.moderator.state.SortOption
import com.example.proyectofinaldisenomovil.core.theme.*

/** Available distance filter presets shown in the Distancia dropdown. */
private val DISTANCE_OPTIONS = listOf(5, 10, 20, 50, 100)

/**
 * Row with two inline dropdowns:
 *  - "Ordenar por: [Nombre ▼]" — changes the [SortOption] applied to the event list.
 *  - "Distancia: [20 km ▼]"   — changes the geographic radius filter.
 *
 * @param selectedSort     Currently active [SortOption].
 * @param selectedDistance Currently active distance in km.
 * @param onSortChange     Called with the new [SortOption] when the user picks one.
 * @param onDistanceChange Called with the new distance (km) when the user picks one.
 */
@Composable
fun SortDistanceRow(
    selectedSort: SortOption,
    selectedDistance: Int,
    onSortChange: (SortOption) -> Unit,
    onDistanceChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // ── Sort dropdown ─────────────────────────────────────────────────────
        InlineDropdown(
            label   = "Ordenar por:",
            current = selectedSort.label,
            options = SortOption.entries.map { it.label },
            onSelect = { label ->
                onSortChange(SortOption.entries.first { it.label == label })
            },
        )

        // ── Distance dropdown ─────────────────────────────────────────────────
        InlineDropdown(
            label   = "Distancia:",
            current = "$selectedDistance km",
            options = DISTANCE_OPTIONS.map { "$it km" },
            onSelect = { label ->
                onDistanceChange(label.replace(" km", "").toInt())
            },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Private helper: a label + value + arrow that expands a DropdownMenu
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun InlineDropdown(
    label: String,
    current: String,
    options: List<String>,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier          = Modifier
                .then(
                    Modifier.clickable { expanded = true }
                ),
        ) {
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.width(4.dp))

            // Outlined chip showing the selected value
            Surface(
                shape  = RoundedCornerShape(6.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                ) {
                    Text(current, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary)
                    Icon(
                        imageVector        = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier           = Modifier.size(16.dp),
                        tint               = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }

        DropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text    = { Text(option, fontSize = 13.sp) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}
