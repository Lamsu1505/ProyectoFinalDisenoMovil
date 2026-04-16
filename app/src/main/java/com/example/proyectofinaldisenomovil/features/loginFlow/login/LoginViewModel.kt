package com.example.proyectofinaldisenomovil.features.loginFlow.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.local.SessionDataStore
import com.example.proyectofinaldisenomovil.data.local.SessionManager
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import com.example.proyectofinaldisenomovil.domain.model.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginResult {
    data object Idle : LoginResult()
    data object Error : LoginResult()
    data object Loading : LoginResult()
    data class Success(val userId: String, val role: UserRole) : LoginResult()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val userRepository: UserRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf("")
    var passwordError by mutableStateOf("")

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> = _loginResult.asStateFlow()

    fun onEmailChange(newEmail: String) {
        email = newEmail
        _loginResult.value = LoginResult.Idle
        validateEmail(email)
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        _loginResult.value = LoginResult.Idle
        validatePassword(password)
    }

    private fun validateEmail(email: String) {
        emailError = when {
            email.isEmpty() -> ""
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "validation_email_invalid"
            else -> ""
        }
    }

    private fun validatePassword(password: String) {
        passwordError = when {
            password.isEmpty() -> ""
            password.length < 8 -> "validation_password_short"
            else -> ""
        }
    }

    fun validateForm(): Boolean {
        return emailError.isEmpty() && passwordError.isEmpty() && 
               email.isNotEmpty() && password.isNotEmpty()
    }

    fun login() {
        Log.i("Login", "La lista actual de usuarios es: " + userRepository.getAllUsers().toString())
        if (!validateForm()) return
        _loginResult.value = LoginResult.Loading

        val user = userRepository.validateCredentials(email, password)
        //val user = MockDataRepository.validateCredentials(email, password)

        if (user != null) {
            viewModelScope.launch {
                val mappedRole = when (user.role) {
                    UserRole.USER -> com.example.proyectofinaldisenomovil.domain.model.UserRole.USER
                    UserRole.MODERATOR -> com.example.proyectofinaldisenomovil.domain.model.UserRole.ADMIN
                }
                sessionDataStore.saveSession(
                    UserSession(
                        userId = user.uid,
                        role = mappedRole
                    )
                )
            }
            MockDataRepository.setLoggedInUser(user) //TODO{Cambiar esto al repositorio}
            _loginResult.value = LoginResult.Success(user.uid, user.role)
        } else {
            _loginResult.value = LoginResult.Error
        }
    }

    fun resetResult() {
        _loginResult.value = LoginResult.Idle
    }
}
