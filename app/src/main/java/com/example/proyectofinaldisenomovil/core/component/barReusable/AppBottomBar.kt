package com.example.proyectofinaldisenomovil.core.component.barReusable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.navigation.AppScreens
import com.example.proyectofinaldisenomovil.core.theme.ProyectoFinalDisenoMovilTheme

@Composable
fun AppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val iconsActualSize = 30.dp

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    var showConfirmDialog by remember { mutableStateOf(false) }

    NavigationBar(
        modifier = modifier,
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.onBackground
    ) {

        //Inicio
        NavigationBarItem(
            selected = currentRoute == "home_route",
            onClick = {
                navController.navigate(AppScreens.HomeScreen.route)
            },
            icon = { Icon(Icons.Default.Home,
                contentDescription = "Inicio",
                modifier = Modifier.size(iconsActualSize)) },
            label = { Text("Inicio") }
        )

        // Iré
        NavigationBarItem(
            selected = currentRoute == "going_route",
            onClick = {
            },
            icon = { Icon(Icons.Default.Bookmark,
                contentDescription = "Iré",
                modifier = Modifier.size(iconsActualSize)) },
            label = { Text("Iré") }
        )

        // Espacio central para FAB
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navController.navigate(AppScreens.CreateEventScreen.route)
                }
            ) {
                Icon(Icons.Default.Add,
                    contentDescription = "Agregar",
                    modifier = Modifier.size(40.dp))
            }
        }

        // Me gusta
        NavigationBarItem(
            selected = currentRoute == "favorites_route",
            onClick = {
                navController.navigate(AppScreens.LikedEventsScreen.route)
            },
            icon = { Icon(Icons.Default.Favorite,
                contentDescription = "Me gusta",
                modifier = Modifier.size(iconsActualSize)) },
            label = { Text("Me gusta") }
        )

        // Perfil
        NavigationBarItem(
            selected = currentRoute == "profile_route",
            onClick = {
                navController.navigate(AppScreens.ProfileScreen.route)
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