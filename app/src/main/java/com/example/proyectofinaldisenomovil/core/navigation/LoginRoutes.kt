package com.example.proyectofinaldisenomovil.core.navigation

import kotlinx.serialization.Serializable

sealed class  LoginRoutes{

    @Serializable
    data object Login : LoginRoutes()


    @Serializable
    data object Register : LoginRoutes()


    @Serializable
    data object ForgotPassword : LoginRoutes()

    @Serializable
    data object RecoverPassword : LoginRoutes()

}