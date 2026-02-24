package com.example.proyectofinaldisenomovil.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.features.screens.FirstScreen
import com.example.proyectofinaldisenomovil.features.screens.SecondScreen


//Se encarga de orquestar la navegacion de la app
@Composable
fun AppNavigation(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.FirstScreen.route)  {
        composable(route = AppScreens.FirstScreen.route){
            FirstScreen(navController)
        }
        composable(route = AppScreens.SecondScreen.route){
            SecondScreen(navController)
        }
    }

}