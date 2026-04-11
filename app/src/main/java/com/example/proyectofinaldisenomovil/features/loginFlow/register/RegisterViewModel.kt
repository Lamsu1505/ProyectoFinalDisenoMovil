package com.example.proyectofinaldisenomovil.features.loginFlow.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.domain.model.User.UserRole

sealed class RegisterResult {
    data object Idle : RegisterResult()
    data object Loading : RegisterResult()
    data object Success : RegisterResult()
    data object EmailAlreadyExists : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

class RegisterViewModel : ViewModel() {

    var name by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordConfirmation by mutableStateOf("")
    
    var nameError by mutableStateOf("")
    var lastNameError by mutableStateOf("")
    var emailError by mutableStateOf("")
    var passwordError by mutableStateOf("")
    var passwordConfirmationError by mutableStateOf("")

    private val _registerResult = mutableStateOf<RegisterResult>(RegisterResult.Idle)
    val registerResult: RegisterResult get() = _registerResult.value

    fun onNameChange(newName: String) {
        name = newName
        nameError = if (name.isNotEmpty() && name.length < 2) "Nombre muy corto" else ""
    }

    fun onLastNameChange(newLastName: String) {
        lastName = newLastName
        lastNameError = if (lastName.isNotEmpty() && lastName.length < 2) "Apellido muy corto" else ""
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        validateEmail(email)
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        validatePassword(password)
        if (passwordConfirmation.isNotEmpty()) {
            validatePasswordConfirmation(passwordConfirmation)
        }
    }

    fun onPasswordConfirmationChange(newConfirmation: String) {
        passwordConfirmation = newConfirmation
        validatePasswordConfirmation(passwordConfirmation)
    }

    private fun validateEmail(email: String) {
        emailError = when {
            email.isEmpty() -> ""
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email mal escrito"
            else -> ""
        }
    }

    private fun validatePassword(password: String) {
        passwordError = when {
            password.isEmpty() -> ""
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            !password.any { it.isUpperCase() } -> "La contraseña debe tener al menos una mayúscula"
            !password.any { it.isDigit() } -> "La contraseña debe tener al menos un número"
            else -> ""
        }
    }

    private fun validatePasswordConfirmation(confirmation: String) {
        passwordConfirmationError = when {
            confirmation.isEmpty() -> ""
            confirmation != password -> "Las contraseñas no coinciden"
            else -> ""
        }
    }

    fun validateForm(): Boolean {
        return nameError.isEmpty() && lastNameError.isEmpty() &&
               emailError.isEmpty() && passwordError.isEmpty() &&
               passwordConfirmationError.isEmpty() &&
               name.isNotEmpty() && lastName.isNotEmpty() &&
               email.isNotEmpty() && password.isNotEmpty() &&
               passwordConfirmation.isNotEmpty()
    }

    fun register() {
        if (!validateForm()) {
            _registerResult.value = RegisterResult.Error("Por favor complete todos los campos correctamente")
            return
        }

        _registerResult.value = RegisterResult.Loading

        val user = MockDataRepository.registerUser(
            firstName = name.trim(),
            lastName = lastName.trim(),
            email = email.trim().lowercase(),
            password = password
        )

        _registerResult.value = when {
            user == null -> RegisterResult.EmailAlreadyExists
            else -> RegisterResult.Success
        }
    }

    fun resetResult() {
        _registerResult.value = RegisterResult.Idle
    }

    fun clearForm() {
        name = ""
        lastName = ""
        email = ""
        password = ""
        passwordConfirmation = ""
        nameError = ""
        lastNameError = ""
        emailError = ""
        passwordError = ""
        passwordConfirmationError = ""
        _registerResult.value = RegisterResult.Idle
    }
}
