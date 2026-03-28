package com.example.proyectofinaldisenomovil.features.loginFlow.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class LoginResult {

    data object Idle : LoginResult() //Inactivo
    data object Error : LoginResult()
    data class Success(val role: UserRole) : LoginResult()
}

class LoginViewModel
    : ViewModel() {

    var email by  mutableStateOf("")
    var password by  mutableStateOf("")
    var emailError by  mutableStateOf("")

    var passwordError by  mutableStateOf("")

    val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    var loginResult: StateFlow<LoginResult> = _loginResult.asStateFlow()


    fun onEmailChange(newEmail: String) {
        this.email = newEmail
        _loginResult.value = LoginResult.Idle
        validateEmail(email)
    }

    fun onPasswordChange(newPassword: String) {
        this.password = newPassword
        _loginResult.value = LoginResult.Idle
        validatePassword(password)
    }

    fun validateEmail(email: String) {
        emailError =
            if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "Email mal escrito"
            } else {
                ""
            }
    }

    fun validatePassword(password : String) {
        if (password.length > 0 && password.length < 8 ) {
            passwordError = "La contraseña debe tener al menos 8 caracteres"
        } else {
            passwordError = ""
        }
    }

    fun validateForm() : Boolean{
        if(emailError.isEmpty() && passwordError.isEmpty() && email.isNotEmpty() && password.isNotEmpty()){
            return true
        }
        return false
    }


    fun login() {
        _loginResult.value = when {
            email == "admin@g.com" && password == "87654321" ->
                LoginResult.Success(UserRole.MODERATOR)

            email == "a@g.com" && password == "12345678" ->
                LoginResult.Success(UserRole.USER)
            else ->
               LoginResult.Error
        }
    }

    fun resetResult() {
        _loginResult.value = LoginResult.Idle
    }

}