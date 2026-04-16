package com.example.proyectofinaldisenomovil.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyectofinaldisenomovil.features.loginFlow.AuthNavigation
import com.example.proyectofinaldisenomovil.features.moderatorFlow.ModeratorNavigation
import com.example.proyectofinaldisenomovil.features.userFlow.UserNavigation

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()

    when (val state = sessionState) {
        is SessionState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is SessionState.NotAuthenticated -> {
            AuthNavigation(
                onSessionSaved = { session ->
                    sessionViewModel.saveSession(session)
                }
            )
        }

        is SessionState.Authenticated -> {
            MainNavigation(
                session = state.session,
                onLogout = {
                    sessionViewModel.clearSession()
                }
            )
        }
    }
}

@Composable
fun MainNavigation(
    session: com.example.proyectofinaldisenomovil.domain.model.UserSession,
    onLogout: () -> Unit
) {
    when (session.role) {
        com.example.proyectofinaldisenomovil.domain.model.UserRole.USER -> {
            UserNavigation(onLogout = onLogout)
        }
        com.example.proyectofinaldisenomovil.domain.model.UserRole.ADMIN -> {
            ModeratorNavigation(onLogout = onLogout)
        }
    }
}
