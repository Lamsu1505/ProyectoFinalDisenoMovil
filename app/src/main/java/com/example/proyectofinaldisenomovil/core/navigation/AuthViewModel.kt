package com.example.proyectofinaldisenomovil.core.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.local.SessionManager
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Loading : AuthState()
    data object NotAuthenticated : AuthState()
    data class Authenticated(val role: UserRole) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        //checkAuthState()
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            sessionManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    val userId = sessionManager.userId.first()
                    val userRole = sessionManager.userRole.first()
                    if (userId != null && userRole != null) {
                        _authState.value = AuthState.Authenticated(UserRole.valueOf(userRole))
                    }
                } else {
                    _authState.value = AuthState.NotAuthenticated
                }
            }
        }
    }
//    private fun checkAuthState() {
//        viewModelScope.launch {
//            val isLoggedIn = sessionManager.isLoggedIn.first()
//            if (isLoggedIn) {
//                val userId = sessionManager.userId.first()
//                val userRole = sessionManager.userRole.first()
//                if (userId != null && userRole != null) {
//                    val user = MockDataRepository.getUserById(userId)
//                    if (user != null) {
//                        MockDataRepository.setLoggedInUser(user)
//                        _authState.value = AuthState.Authenticated(UserRole.valueOf(userRole))
//                    } else {
//                        sessionManager.clearSession()
//                        _authState.value = AuthState.NotAuthenticated
//                    }
//                } else {
//                    _authState.value = AuthState.NotAuthenticated
//                }
//            } else {
//                _authState.value = AuthState.NotAuthenticated
//            }
//        }
//    }

    fun logout() {
        viewModelScope.launch {
            Log.i("DEBUG", "Iniciando proceso de Logout")
            sessionManager.clearSession()
            MockDataRepository.logout()
            _authState.value = AuthState.NotAuthenticated
            Log.i("DEBUG", "authState cambiado a NotAuthenticated")
        }
    }
}