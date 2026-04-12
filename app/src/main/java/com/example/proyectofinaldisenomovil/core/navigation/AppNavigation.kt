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
import com.example.proyectofinaldisenomovil.features.userFlow.UserNavigation
import com.example.proyectofinaldisenomovil.features.userFlow.ViewEvent.ViewEventScreen
import com.example.proyectofinaldisenomovil.features.moderatorFlow.ModeratorNavigation

object LoginRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val RECOVER_PASSWORD = "recover_password"
}

object AppRoutes {
    const val USER_FLOW = "user_flow"
    const val MODERATOR_FLOW = "moderator_flow"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoutes.LOGIN
    ) {

        composable(LoginRoutes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(LoginRoutes.REGISTER) },
                onNavigateToForgotPassword = { navController.navigate(LoginRoutes.FORGOT_PASSWORD) },
                onNavigateToUserFLow = {
                    navController.navigate(AppRoutes.USER_FLOW) {
                        popUpTo(LoginRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToModeratorFlow = {
                    navController.navigate(AppRoutes.MODERATOR_FLOW) {
                        popUpTo(LoginRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(LoginRoutes.REGISTER) {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(LoginRoutes.LOGIN) }
            )
        }

        composable(LoginRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToRecoverPassword = { navController.navigate(LoginRoutes.RECOVER_PASSWORD) }
            )
        }

        composable(LoginRoutes.RECOVER_PASSWORD) {
            RecoverPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(LoginRoutes.LOGIN) },
                onSubmit = { navController.navigate(LoginRoutes.LOGIN) }
            )
        }

        composable(AppRoutes.USER_FLOW) {
            UserNavigation(
                onLogout = {
                    navController.navigate(LoginRoutes.LOGIN) {
                        popUpTo(AppRoutes.USER_FLOW) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.MODERATOR_FLOW) {
            ModeratorNavigation(
                onLogout = {
                    navController.navigate(LoginRoutes.LOGIN) {
                        popUpTo(AppRoutes.MODERATOR_FLOW) { inclusive = true }
                    }
                }
            )
        }
    }
}
