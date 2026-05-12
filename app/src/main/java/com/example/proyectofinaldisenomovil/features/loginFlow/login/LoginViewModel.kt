package com.example.proyectofinaldisenomovil.features.loginFlow.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.local.SessionDataStore
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.R
import com.example.proyectofinaldisenomovil.core.utils.RequestResult
import com.example.proyectofinaldisenomovil.core.utils.ResourceProvider
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import com.example.proyectofinaldisenomovil.domain.model.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf("")
    var passwordError by mutableStateOf("")

    private val _loginResult = MutableStateFlow<RequestResult?>(null)
    val loginResult: StateFlow<RequestResult?> = _loginResult.asStateFlow()

    fun onEmailChange(newEmail: String) {
        email = newEmail
        resetResult()
        validateEmail(email)
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        resetResult()
        validatePassword(password)
    }

    private fun validateEmail(email: String) {
        emailError = when {
            email.isEmpty() -> ""
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> resourceProvider.getString(R.string.validation_email_invalid)
            else -> ""
        }
    }

    private fun validatePassword(password: String) {
        passwordError = when {
            password.isEmpty() -> ""
            password.length < 8 -> resourceProvider.getString(R.string.validation_password_short)
            else -> ""
        }
    }

    fun validateForm(): Boolean {
        return emailError.isEmpty() && passwordError.isEmpty() && 
               email.isNotEmpty() && password.isNotEmpty()
    }

    fun login() {
        if (validateForm()) {
            viewModelScope.launch {
                // Indicar que la operación está en curso
                _loginResult.value = RequestResult.Loading
                // Llamar a la función de login del repositorio
                val user = userRepository.login(email, password)

                if(user!=null){
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
                    _loginResult.value = RequestResult.Success(R.string.login_success.toString())
                }
                else {
                    _loginResult.value = RequestResult.Failure(R.string.login_error.toString())
                }
            }
        }
    }


    fun resetResult() {
        _loginResult.value = null
    }
}