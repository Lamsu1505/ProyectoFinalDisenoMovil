package com.example.proyectofinaldisenomovil.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.features.ForgotPassword.ForgotPasswordScreen
import com.example.proyectofinaldisenomovil.features.RecoverPassword.RecoverPasswordScreen
import com.example.proyectofinaldisenomovil.features.home.HomeScreen
import com.example.proyectofinaldisenomovil.features.login.LoginScreen
import com.example.proyectofinaldisenomovil.features.login.LoginViewModel
import com.example.proyectofinaldisenomovil.features.register.RegisterScreen


//Se encarga de orquestar la navegacion de la app
@Composable
fun AppNavigation(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route)  {
        composable ( route = AppScreens.LoginScreen.route){
            LoginScreen(navController)
        }
        composable ( route = AppScreens.RegisterScreen.route){
            RegisterScreen(navController)
        }
        composable ( route = AppScreens.HomeScreen.route){
            HomeScreen(navController)
        }
        composable ( route = AppScreens.ForgotPasswordScreen.route){
            ForgotPasswordScreen(navController)
        }
        composable ( route = AppScreens.RecoverPasswordScreen.route){
            RecoverPasswordScreen(navController)
        }


    }

}