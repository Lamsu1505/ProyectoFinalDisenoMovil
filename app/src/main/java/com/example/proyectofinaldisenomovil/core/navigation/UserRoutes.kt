package com.example.proyectofinaldisenomovil.core.navigation

import kotlinx.serialization.Serializable

sealed class  UserRoutes {
    @Serializable
    data object Home : UserRoutes()

    @Serializable
    data object SavedEvents : UserRoutes()

    @Serializable
    data object CreateEvent : UserRoutes()

    @Serializable
    data object LikedEvents : UserRoutes()

    @Serializable
    data object Profile : UserRoutes()

    @Serializable
    data object Notifications : UserRoutes()
}