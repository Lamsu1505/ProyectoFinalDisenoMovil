package com.example.proyectofinaldisenomovil.core.navigation

import kotlinx.serialization.Serializable

sealed class ModeratorRoutes {
    @Serializable
    object Panel : ModeratorRoutes()

    @Serializable
    data class EventDetail(val eventId: String) : ModeratorRoutes()
}
