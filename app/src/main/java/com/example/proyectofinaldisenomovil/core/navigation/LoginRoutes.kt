package com.example.proyectofinaldisenomovil.core.navigation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

sealed class LoginRoutes {

    @Serializable
    data object Login : LoginRoutes()

    @Serializable
    data object Register : LoginRoutes()

    @Serializable
    data object ForgotPassword : LoginRoutes()

    @SuppressLint("UnsafeOptInUsageError")
    @Serializable
    data class RecoverPassword(val email: String) : LoginRoutes()

    @Serializable
    data object UserFlow : LoginRoutes()

    @Serializable
    data object ModeratorFlow : LoginRoutes()
}