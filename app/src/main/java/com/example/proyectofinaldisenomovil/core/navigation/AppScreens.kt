package com.example.proyectofinaldisenomovil.core.navigation

sealed class  AppScreens (val route: String) {
    object FirstScreen : AppScreens("pantalla1")
    object SecondScreen : AppScreens("pantalla2")
    object LoginScreen : AppScreens("login")
}