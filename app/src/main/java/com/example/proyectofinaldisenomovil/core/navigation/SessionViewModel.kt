package com.example.proyectofinaldisenomovil.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.local.SessionDataStore
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SessionState {
    data object Loading : SessionState
    data object NotAuthenticated : SessionState
    data class Authenticated(val session: UserSession) : SessionState
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val userRepository: UserRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            sessionDataStore.sessionFlow.collect { session ->
                if (session != null) {
                    // Si hay sesión guardada en disco, buscamos al usuario real
                    val user = userRepository.getUserById(session.userId)
                    // Y lo ponemos en el MockDataRepository para que el resto de la app lo vea
                    MockDataRepository.setLoggedInUser(user)
                } else {
                    MockDataRepository.setLoggedInUser(null)
                }
            }
        }
    }

    val sessionState: StateFlow<SessionState> = sessionDataStore.sessionFlow
        .map { session ->
            if (session != null) {
                SessionState.Authenticated(session)
            } else {
                SessionState.NotAuthenticated
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionState.Loading
        )

    fun saveSession(session: UserSession) {
        viewModelScope.launch {
            sessionDataStore.saveSession(session)
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            sessionDataStore.clearSession()
        }
    }
}
