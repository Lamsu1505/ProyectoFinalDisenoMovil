package com.example.proyectofinaldisenomovil.features.loginFlow

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectofinaldisenomovil.core.navigation.LoginRoutes
import com.example.proyectofinaldisenomovil.domain.model.UserRole
import com.example.proyectofinaldisenomovil.domain.model.UserSession
import com.example.proyectofinaldisenomovil.features.loginFlow.ForgotPassword.ForgotPasswordScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.RecoverPassword.RecoverPasswordScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.login.LoginScreen
import com.example.proyectofinaldisenomovil.features.loginFlow.register.RegisterScreen

@Composable
fun AuthNavigation(
    onSessionSaved: (UserSession) -> Unit,
    navController: NavHostController = rememberNavController()
) {
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
                onLoginSuccess = { userId, role ->
                    onSessionSaved(UserSession(userId = userId, role = role))
                },
                onNavigateToUserFlow = {
                    navController.navigate(LoginRoutes.UserFlow) {
                        popUpTo<LoginRoutes.Login> { inclusive = true }
                    }
                },
                onNavigateToModeratorFlow = {
                    navController.navigate(LoginRoutes.ModeratorFlow) {
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
    }
}
