package com.example.proyectofinaldisenomovil.core.navigation

sealed class  AppScreens (val route: String) {
    object LoginScreen : AppScreens("loginScreen")
    object RegisterScreen : AppScreens("registerScreen")
    object FirstScreen : AppScreens("firstScreen")
    object HomeScreen : AppScreens("homeScreen")
    object  ForgotPasswordScreen : AppScreens("forgotPasswordScreen")
    object  RecoverPasswordScreen : AppScreens("recoverPasswordScreen")
    object  ViewEventScreen : AppScreens("viewEventScreen")
    object  NotificationsScreen : AppScreens("notificationsScreen")
    object  ProfileScreen : AppScreens("profileScreen")
    object  EditProfileScreen : AppScreens("editprofileScreen")
    object  CreateEventScreen : AppScreens("createEventScreen")
    object  LikedEventsScreen : AppScreens("likedEventsScreen")




}