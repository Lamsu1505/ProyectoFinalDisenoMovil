package com.example.proyectofinaldisenomovil.core.navigation

import kotlinx.serialization.Serializable

//sealed class  AppRoutes (val route: String) {
//    object LoginScreen : AppRoutes("loginScreen")
//    object RegisterScreen : AppRoutes("registerScreen")
//    object FirstScreen : AppRoutes("firstScreen")
//    object HomeScreen : AppRoutes("homeScreen")
//    object  ForgotPasswordScreen : AppRoutes("forgotPasswordScreen")
//    object  RecoverPasswordScreen : AppRoutes("recoverPasswordScreen")
//    object  ViewEventScreen : AppRoutes("viewEventScreen")
//    object  NotificationsScreen : AppRoutes("notificationsScreen")
//    object  ProfileScreen : AppRoutes("profileScreen")
//    object  EditProfileScreen : AppRoutes("editprofileScreen")
//    object  CreateEventScreen : AppRoutes("createEventScreen")
//    object  LikedEventsScreen : AppRoutes("likedEventsScreen")
//    object  SavedEventsScreen : AppRoutes("savedEventsScreen")
//}


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