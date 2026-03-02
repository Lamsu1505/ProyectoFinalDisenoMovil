package com.example.proyectofinaldisenomovil.core.navigation

sealed class  AppScreens (val route: String) {
    object LoginScreen : AppScreens("loginScreen")
    object RegisterScreen : AppScreens("registerScreen")
    object FirstScreen : AppScreens("firstScreen")
}