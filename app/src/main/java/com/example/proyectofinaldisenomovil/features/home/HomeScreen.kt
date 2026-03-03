package com.example.proyectofinaldisenomovil.features.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.component.barReusable.AppBottomBar
import com.example.proyectofinaldisenomovil.core.component.barReusable.BottomNavItem

@Composable
fun HomeScreen (
    navController: NavController
){
    val bottomItems = listOf(
        BottomNavItem("home", Icons.Default.Home, "Inicio"),
    BottomNavItem("favorites", Icons.Default.Favorite, "Me gusta"),
    )
    AppBottomBar(navController , bottomItems)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(navController = rememberNavController())
}