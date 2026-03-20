package com.example.proyectofinaldisenomovil.core.navigation

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



sealed class  UserRoutes (val route: String) {
    object RegisterScreen : UserRoutes("registerScreen")
    object FirstScreen : UserRoutes("firstScreen")
    object HomeScreen : UserRoutes("homeScreen")
    object  ForgotPasswordScreen : UserRoutes("forgotPasswordScreen")
    object  RecoverPasswordScreen : UserRoutes("recoverPasswordScreen")
    object  ViewEventScreen : UserRoutes("viewEventScreen")
    object  NotificationsScreen : UserRoutes("notificationsScreen")
    object  ProfileScreen : UserRoutes("profileScreen")
    object  EditProfileScreen : UserRoutes("editprofileScreen")
    object  CreateEventScreen : UserRoutes("createEventScreen")
    object  LikedEventsScreen : UserRoutes("likedEventsScreen")
    object  SavedEventsScreen : UserRoutes("savedEventsScreen")
}