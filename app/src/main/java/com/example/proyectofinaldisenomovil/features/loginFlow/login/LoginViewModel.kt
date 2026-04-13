package com.example.proyectofinaldisenomovil.features.loginFlow.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class LoginResult {
    data object Idle : LoginResult()
    data object Error : LoginResult()
    data object Loading : LoginResult()
    data class Success(val role: UserRole) : LoginResult()
}

class LoginViewModel : ViewModel() {

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
        if (!validateForm()) return
        
        _loginResult.value = LoginResult.Loading
        
        val user = MockDataRepository.validateCredentials(email, password)
        
        _loginResult.value = if (user != null) {
            MockDataRepository.setLoggedInUser(user)
            LoginResult.Success(user.role)
        } else {
            LoginResult.Error
        }
    }

    fun resetResult() {
        _loginResult.value = LoginResult.Idle
    }
}
