package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

        // 1️⃣ Inicio
        NavigationBarItem(
            selected = currentRoute == "home_route",
            onClick = {
                navController.navigate("home_route") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home,
                contentDescription = "Inicio",
                modifier = Modifier.size(iconsActualSize)) },
            label = { Text("Inicio") }
        )

        // 2️⃣ Iré
        NavigationBarItem(
            selected = currentRoute == "going_route",
            onClick = {
                navController.navigate("going_route") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Bookmark,
                contentDescription = "Iré",
                modifier = Modifier.size(iconsActualSize)) },
            label = { Text("Iré") }
        )

        // 3️⃣ Espacio central para FAB
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navController.navigate("add_route") {
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(Icons.Default.Add,
                    contentDescription = "Agregar",
                    modifier = Modifier.size(40.dp))
            }
        }

        // 4️⃣ Me gusta
        NavigationBarItem(
            selected = currentRoute == "favorites_route",
            onClick = {
                navController.navigate("favorites_route") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Favorite,
                contentDescription = "Me gusta",
                modifier = Modifier.size(iconsActualSize)) },
            label = { Text("Me gusta") }
        )

        // 5️⃣ Perfil
        NavigationBarItem(
            selected = currentRoute == "profile_route",
            onClick = {
                navController.navigate("profile_route") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Person,
                contentDescription = "Perfil",
                modifier = Modifier.size(iconsActualSize)) },
            label = { Text("Perfil") }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAppBottomBar() {
    ProyectoFinalDisenoMovilTheme() {
        AppBottomBar(navController = rememberNavController())
    }
}