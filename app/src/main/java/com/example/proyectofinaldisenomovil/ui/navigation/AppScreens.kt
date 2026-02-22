package com.example.proyectofinaldisenomovil.ui.navigation

sealed class  AppScreens (val route: String) {
    object FirstScreen : AppScreens("pantalla1")
    object SecondScreen : AppScreens("pantalla2")
}