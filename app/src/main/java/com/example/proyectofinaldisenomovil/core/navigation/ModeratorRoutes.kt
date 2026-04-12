package com.example.proyectofinaldisenomovil.core.navigation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

sealed class ModeratorRoutes {
    @Serializable
    object Panel : ModeratorRoutes()

    @SuppressLint("UnsafeOptInUsageError")
    @Serializable
    data class EventDetail(val eventId: String) : ModeratorRoutes()
}
