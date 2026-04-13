package com.example.proyectofinaldisenomovil.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectofinaldisenomovil.features.userFlow.CreateEvent.CreateEventScreen
import com.example.proyectofinaldisenomovil.features.userFlow.EditProfile.EditProfileScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.ForgotPassword.ForgotPasswordScreen
import com.example.proyectofinaldisenomovil.features.userFlow.LikedEvents.LikedEventsScreen
import com.example.proyectofinaldisenomovil.features.LikedEvents.SavedEventsScreen
import com.example.proyectofinaldisenomovil.features.userFlow.Notifications.NotificationsScreen
import com.example.proyectofinaldisenomovil.features.userFlow.Profile.ProfileScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.RecoverPassword.RecoverPasswordScreen
import com.example.proyectofinaldisenomovil.features.userFlow.home.HomeScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.login.LoginScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.register.RegisterScreen
import com.example.proyectofinaldisenomovil.features.userFlow.UserNavigation
import com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent.ViewEventScreen
import com.example.proyectofinaldisenomovil.features.moderatorFlow.ModeratorNavigation

sealed class LoginRoute(val route: String) {
    data object Login : LoginRoute("login")
    data object Register : LoginRoute("register")
    data object ForgotPassword : LoginRoute("forgot_password")
    data object RecoverPassword : LoginRoute("recover_password/{email}") {
        fun createRoute(email: String) = "recover_password/$email"
    }
}

sealed class AppRoute(val route: String) {
    data object UserFlow : AppRoute("user_flow")
    data object ModeratorFlow : AppRoute("moderator_flow")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute.Login.route
    ) {

        composable(LoginRoute.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(LoginRoute.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(LoginRoute.ForgotPassword.route) },
                onNavigateToUserFLow = {
                    navController.navigate(AppRoute.UserFlow.route) {
                        popUpTo(LoginRoute.Login.route) { inclusive = true }
                    }
                },
                onNavigateToModeratorFlow = {
                    navController.navigate(AppRoute.ModeratorFlow.route) {
                        popUpTo(LoginRoute.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(LoginRoute.Register.route) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(LoginRoute.Login.route) }
            )
        }

        composable(LoginRoute.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToRecoverPassword = { email ->
                    navController.navigate(LoginRoute.RecoverPassword.createRoute(email))
                }
            )
        }

        composable(
            route = LoginRoute.RecoverPassword.route,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            RecoverPasswordScreen(
                email = email,
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(LoginRoute.Login.route) },
                onSubmit = { navController.navigate(LoginRoute.Login.route) }
            )
        }

        composable(AppRoute.UserFlow.route) {
            UserNavigation(
                onLogout = {
                    navController.navigate(LoginRoute.Login.route) {
                        popUpTo(AppRoute.UserFlow.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.ModeratorFlow.route) {
            ModeratorNavigation(
                onLogout = {
                    navController.navigate(LoginRoute.Login.route) {
                        popUpTo(AppRoute.ModeratorFlow.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
