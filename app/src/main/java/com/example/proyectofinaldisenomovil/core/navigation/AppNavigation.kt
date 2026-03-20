package com.example.proyectofinaldisenomovil.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent.ViewEventScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoutes.Login
    ) {

        composable<LoginRoutes.Login> {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(LoginRoutes.Register)},
                onNavigateToForgotPassword = { navController.navigate((LoginRoutes.ForgotPassword))},
                onLoginSuccess = {}
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
    }
}