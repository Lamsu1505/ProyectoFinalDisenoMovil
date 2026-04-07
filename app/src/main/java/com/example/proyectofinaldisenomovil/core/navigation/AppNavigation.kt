package com.example.proyectofinaldisenomovil.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.core.navigation.LoginRoutes
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


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoutes.Login
    ) {

        // LOGIN FLOW
        composable<LoginRoutes.Login> {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(LoginRoutes.Register) },
                onNavigateToForgotPassword = { navController.navigate(LoginRoutes.ForgotPassword) },
                onNavigateToUserFLow = {
                    navController.navigate(AppRoutes.UserFlow) {
                        popUpTo(LoginRoutes.Login) { inclusive = true }
                    }
                },
                onNavigateToModeratorFlow = {
                    navController.navigate(AppRoutes.ModeratorFlow) {
                        popUpTo(LoginRoutes.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<LoginRoutes.Register> {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(LoginRoutes.Login) }
            )
        }

        composable<LoginRoutes.ForgotPassword> {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToRecoverPassword = { navController.navigate(LoginRoutes.RecoverPassword) }
            )
        }

        composable<LoginRoutes.RecoverPassword> {
            RecoverPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(LoginRoutes.Login) },
                onSubmit = { navController.navigate(LoginRoutes.Login) }
            )
        }

        // USER FLOW
        composable<AppRoutes.UserFlow> {
            UserNavigation(
                onLogout = {
                    navController.navigate(LoginRoutes.Login) {
                        popUpTo(AppRoutes.UserFlow) { inclusive = true }
                    }
                }
            )
        }

        // MODERATOR FLOW
        composable<AppRoutes.ModeratorFlow> {
            ModeratorNavigation(
                onLogout = {
                    navController.navigate(LoginRoutes.Login) {
                        popUpTo(AppRoutes.ModeratorFlow) { inclusive = true }
                    }
                }
            )
        }
    }
}