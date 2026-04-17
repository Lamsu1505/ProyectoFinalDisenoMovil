package com.example.proyectofinaldisenomovil.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinaldisenomovil.domain.model.UserRole
import com.example.proyectofinaldisenomovil.features.loginFlow.AuthNavigation
import com.example.proyectofinaldisenomovil.features.moderatorFlow.ModeratorNavigation
import com.example.proyectofinaldisenomovil.features.userFlow.UserNavigation

sealed class AppRoute {
    @kotlinx.serialization.Serializable
    data object AuthFlow : AppRoute()
    @kotlinx.serialization.Serializable
    data object UserFlow : AppRoute()
    @kotlinx.serialization.Serializable
    data object ModeratorFlow : AppRoute()
}

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val navController = rememberNavController()

    // Lógica para manejar la persistencia de sesión al arrancar la app
    LaunchedEffect(sessionState) {
        when (val state = sessionState) {
            is SessionState.Authenticated -> {
                // Decidimos a qué flujo ir basado en el rol guardado
                val destination = if (state.session.role == UserRole.ADMIN) {
                    AppRoute.ModeratorFlow
                } else {
                    AppRoute.UserFlow
                }
                
                // Navegamos y limpiamos el flujo de autenticación del historial
                navController.navigate(destination) {
                    popUpTo(AppRoute.AuthFlow) { inclusive = true }
                }
            }
            is SessionState.NotAuthenticated -> {
                // Si el usuario no está autenticado o cerró sesión, lo mandamos al login
                // Verificamos que no estemos ya ahí para evitar bucles
                if (navController.currentBackStackEntry?.destination?.route != AppRoute.AuthFlow::class.qualifiedName) {
                    navController.navigate(AppRoute.AuthFlow) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            else -> {} // Caso Loading: se maneja abajo con el overlay
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoute.AuthFlow
    ) {
        composable<AppRoute.AuthFlow> {
            AuthNavigation(
                onSessionSaved = { session ->
                    sessionViewModel.saveSession(session)
                },
                onNavigateToUser = {
                    navController.navigate(AppRoute.UserFlow) {
                        popUpTo(AppRoute.AuthFlow) { inclusive = true }
                    }
                },
                onNavigateToModerator = {
                    navController.navigate(AppRoute.ModeratorFlow) {
                        popUpTo(AppRoute.AuthFlow) { inclusive = true }
                    }
                }
            )
        }

        composable<AppRoute.UserFlow> {
            UserNavigation(
                onLogout = {
                    sessionViewModel.clearSession()
                }
            )
        }

        composable<AppRoute.ModeratorFlow> {
            ModeratorNavigation(
                onLogout = {
                    sessionViewModel.clearSession()
                }
            )
        }
    }

    // Pantalla de carga mientras se recupera la sesión del DataStore
    if (sessionState is SessionState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
