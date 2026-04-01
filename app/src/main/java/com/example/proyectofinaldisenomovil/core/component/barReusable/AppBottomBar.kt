package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onCreateEvent: () -> Unit = {},
    onSavedEvents: () -> Unit = {},
    onLikedEvents: () -> Unit = {},
    onProfile: () -> Unit = {},
    selectedRoute: String
) {
    val iconsActualSize = 30.dp

    NavigationBar(
        modifier = modifier,
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.onBackground
    ) {
        // Inicio
        BottomNavItem(
            selected = selectedRoute == "home",
            onClick = {
                onHomeClick()
            },
            icon = Icons.Default.Home,
            label = "Inicio",
            iconSize = iconsActualSize,
        )

        // Iré
        BottomNavItem(
            selected = selectedRoute == "savedEvents",
            onClick = {
                onSavedEvents()
            },
            icon = Icons.Default.Bookmark,
            label = "Iré",
            iconSize = iconsActualSize
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val fabInteractionSource = remember { MutableInteractionSource() }
            val isFabHovered by fabInteractionSource.collectIsHoveredAsState()
            val fabScale by animateFloatAsState(if (isFabHovered) 1.15f else 1f, label = "fab_scale")

            FloatingActionButton(
                modifier = Modifier.scale(fabScale),
                interactionSource = fabInteractionSource,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    onCreateEvent()
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(40.dp))
            }
        }

        // Me gusta
        BottomNavItem(
            selected = selectedRoute == "likedEvents",
            onClick = {
                onLikedEvents()
            },
            icon = Icons.Default.Favorite,
            label = "Me gusta",
            iconSize = iconsActualSize
        )

        // Perfil
        BottomNavItem(
            selected = selectedRoute == "profile",
            onClick = {
                onProfile()
            },
            icon = Icons.Default.Person,
            label = "Perfil",
            iconSize = iconsActualSize
        )
    }
}

@Composable
fun RowScope.BottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    iconSize: androidx.compose.ui.unit.Dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    // Animación de escala para el icono cuando hay hover o está presionado
    val scale by animateFloatAsState(
        targetValue = if (isHovered || isPressed) 1.2f else 1f,
        label = "nav_item_scale"
    )

    // Colores personalizados solicitados: Texto verde y fondo gris para el estado seleccionado
    val selectedGreen = Color(0xFF4A8C5C) // El verde de tu paleta
    val selectedGray = Color(0xFFE0E0E0)  // Un gris claro para el fondo (indicator)

    NavigationBarItem(
        selected = selected,
        onClick = {
            onClick()
        },
        interactionSource = interactionSource,
        colors = NavigationBarItemDefaults.colors(
            // Estado SELECCIONADO: Texto verde y fondo (indicator) gris
            selectedIconColor = selectedGreen,
            selectedTextColor = selectedGreen,
            indicatorColor = selectedGray,
            
            // Estado NO SELECCIONADO: Gris/OnSurfaceVariant (con feedback verde si hay hover)
            unselectedIconColor = if (isHovered) selectedGreen.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = if (isHovered) selectedGreen.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier
                    .size(iconSize)
                    .scale(scale)
            )
        },
        label = { 
            Text(
                text = label,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            ) 
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAppBottomBar() {
    ProyectoFinalDisenoMovilTheme {
        AppBottomBar(
            onHomeClick = {},
            onCreateEvent = {},
            onSavedEvents = {},
            onLikedEvents = {},
            onProfile = {},
            selectedRoute = "home"
        )
    }
}
