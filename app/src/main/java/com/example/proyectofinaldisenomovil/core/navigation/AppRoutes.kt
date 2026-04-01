package com.example.proyectofinaldisenomovil.core.navigation

import kotlinx.serialization.Serializable

sealed class AppRoutes {

    @Serializable
    object LoginFlow : AppRoutes()

    @Serializable
    object UserFlow : AppRoutes()

    @Serializable
    object ModeratorFlow : AppRoutes()
}