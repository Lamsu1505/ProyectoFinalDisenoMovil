package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun AppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = modifier,
        tonalElevation = 8.dp
    ) {

        // 1️⃣ Inicio
        NavigationBarItem(
            selected = currentRoute == "home_route",
            onClick = {
                navController.navigate("home_route") {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
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
            icon = { Icon(Icons.Default.Bookmark, contentDescription = "Iré") },
            label = { Text("Iré") }
        )

        // 3️⃣ Espacio central para FAB
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_route") {
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
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
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Me gusta") },
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
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}