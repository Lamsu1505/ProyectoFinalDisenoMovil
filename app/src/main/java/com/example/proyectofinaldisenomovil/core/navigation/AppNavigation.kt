package com.example.proyectofinaldisenomovil.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectofinaldisenomovil.features.loginFlow.ForgotPassword.ForgotPasswordScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.RecoverPassword.RecoverPasswordScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.login.LoginScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.register.RegisterScreen
import com.example.proyectofinaldisenomovil.features.userFlow.UserNavigation
import com.example.proyectofinaldisenomovil.features.moderatorFlow.ModeratorNavigation

sealed class AppRoute {
    @kotlinx.serialization.Serializable
    data object UserFlow : AppRoute()
    @kotlinx.serialization.Serializable
    data object ModeratorFlow : AppRoute()
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    // Lógica para reaccionar al cambio de estado de autenticación
    LaunchedEffect(authState) {
        if (authState is AuthState.NotAuthenticated) {
            if (navController.currentDestination?.route != LoginRoutes.Login::class.qualifiedName) {
                navController.navigate(LoginRoutes.Login) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    val onLogout: () -> Unit = {
        authViewModel.logout()
    }

    NavHost(
        navController = navController,
        startDestination = LoginRoutes.Login
    ) {
        composable<LoginRoutes.Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(LoginRoutes.Register)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(LoginRoutes.ForgotPassword)
                },
                onNavigateToUserFLow = {
                    navController.navigate(AppRoute.UserFlow) {
                        popUpTo<LoginRoutes.Login> { inclusive = true }
                    }
                },
                onNavigateToModeratorFlow = {
                    navController.navigate(AppRoute.ModeratorFlow) {
                        popUpTo<LoginRoutes.Login> { inclusive = true }
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
                onNavigateToRecoverPassword = { email ->
                    navController.navigate(LoginRoutes.RecoverPassword(email))
                }
            )
        }

        composable<LoginRoutes.RecoverPassword> { backStackEntry ->
            val route = backStackEntry.toRoute<LoginRoutes.RecoverPassword>()
            RecoverPasswordScreen(
                email = route.email,
                onBackClick = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(LoginRoutes.Login) },
                onSubmit = { navController.navigate(LoginRoutes.Login) }
            )
        }

        composable<AppRoute.UserFlow> {
            UserNavigation(
                onLogout = onLogout
            )
        }

        composable<AppRoute.ModeratorFlow> {
            ModeratorNavigation(onLogout = onLogout)
        }
    }
}