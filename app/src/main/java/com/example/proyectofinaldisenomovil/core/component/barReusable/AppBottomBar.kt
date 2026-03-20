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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.navigation.UserRoutes
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun AppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val iconsActualSize = 30.dp
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = modifier,
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.onBackground
    ) {
        // Inicio
        BottomNavItem(
            selected = currentRoute == "home_route" || currentRoute == UserRoutes.HomeScreen.route,
            onClick = { navController.navigate(UserRoutes.HomeScreen.route) },
            icon = Icons.Default.Home,
            label = "Inicio",
            iconSize = iconsActualSize
        )

        // Iré
        BottomNavItem(
            selected = currentRoute == "going_route" || currentRoute == UserRoutes.SavedEventsScreen.route,
            onClick = { navController.navigate(UserRoutes.SavedEventsScreen.route) },
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
                    navController.navigate(UserRoutes.CreateEventScreen.route)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(40.dp))
            }
        }

        // Me gusta
        BottomNavItem(
            selected = currentRoute == "favorites_route" || currentRoute == UserRoutes.LikedEventsScreen.route,
            onClick = { navController.navigate(UserRoutes.LikedEventsScreen.route) },
            icon = Icons.Default.Favorite,
            label = "Me gusta",
            iconSize = iconsActualSize
        )

        // Perfil
        BottomNavItem(
            selected = currentRoute == "profile_route" || currentRoute == UserRoutes.ProfileScreen.route,
            onClick = { navController.navigate(UserRoutes.ProfileScreen.route) },
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

    val scale by animateFloatAsState(
        targetValue = if (isHovered || isPressed) 1.2f else 1f,
        label = "nav_item_scale"
    )

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        interactionSource = interactionSource,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier
                    .size(iconSize)
                    .scale(scale)
            )
        },
        label = { Text(label) }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAppBottomBar() {
    ProyectoFinalDisenoMovilTheme {
        AppBottomBar(navController = rememberNavController())
    }
}